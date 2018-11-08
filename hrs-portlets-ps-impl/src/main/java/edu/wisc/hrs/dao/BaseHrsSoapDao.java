/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package edu.wisc.hrs.dao;

import java.util.Iterator;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.ws.client.WebServiceFaultException;
import org.springframework.ws.client.core.WebServiceOperations;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.SoapFaultDetailElement;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.w3c.dom.Node;

import org.jasig.springframework.ws.client.core.SetSoapActionCallback;

/**
 * Base class for HRS Web Services. Provides common invocation and error handling logic
 * 
 * @author Eric Dalquist
 */
public abstract class BaseHrsSoapDao {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private SetSoapActionCallback soapActionCallback;


    @Required
    public void setSoapAction(String soapAction) {
        this.soapActionCallback = new SetSoapActionCallback(soapAction);
    }

    protected abstract WebServiceOperations getWebServiceOperations();

    /**
     * Calls {@link WebServiceTemplate#marshalSendAndReceive(Object)} on the passed
     * in argument, and returns the result.
     * 
     * In the event a SOAP Fault occurs (e.g. {@link SoapFaultClientException}, this method
     * inspects the faultStringOrReason field. 
     * Based on the contents of that field, this method may throw a {@link ClientRuntimeException} or
     * return null.
     * 
     * null return values are used as hints to callers that they should throw either the checked
     * {@link ClassNotFoundException}.
     * 
     * @param request
     * @return the result of {@link WebServiceTemplate#marshalSendAndReceive(Object)}, or null if the caller should throw a {@link ClassNotFoundException}
     * @throws AuthenticationFailedException if the username/password configured on the webServiceTemplate are incorrect
     * @throws AuthorizationFailedException if the user is authenticated but not authorized to call the particular method
     * @throws WebServiceFaultException for other soap faults
     */
    @SuppressWarnings("unchecked")
    protected <T> T internalInvoke(Object request) {
        final WebServiceOperations webServiceOperations = this.getWebServiceOperations();
		try {
            return (T)webServiceOperations.marshalSendAndReceive(request, this.soapActionCallback);
		}
		catch (SoapFaultClientException e) {
		    final StringBuilder errorMessage = new StringBuilder("Failed to call ");
		    errorMessage.append(this.soapActionCallback.getSoapAction());
		    errorMessage.append(" with ");
		    errorMessage.append(request);
		    
		    final SoapFault soapFault = e.getSoapFault();
		    final SoapFaultDetail faultDetail = soapFault.getFaultDetail();
		    
		    for (final Iterator<SoapFaultDetailElement> detailEntryItr = faultDetail.getDetailEntries(); detailEntryItr.hasNext();) {
		        final SoapFaultDetailElement detailEntry = detailEntryItr.next();
		        final Source source = detailEntry.getSource();
		        
//		        TODO Once PS adds a namespace declaration to the fault document unmarshaller can be used to pull out the error message
//              final Object result = this.unmarshaller.unmarshal(source);
		        
		        final Node node = this.getNode(source);
		        if (node == null) {
		            //Couldn't turn the source into a node, no detailed error message for us :(
		            continue;
		        }

                final String defaultTitle = evaluateXPath(node, "DefaultTitle");
                final String defaultMessage = evaluateXPath(node, "DefaultMessage");

                errorMessage.append("\n\t").append(defaultTitle).append(" - ").append(defaultMessage);
		    }
		    
		    
            final WebServiceFaultException webServiceFaultException = new WebServiceFaultException(errorMessage.toString());
            webServiceFaultException.initCause(e);
            throw webServiceFaultException;
		}
	}
    
    protected Node getNode(Source source) {
        if (source instanceof DOMSource) {
            return ((DOMSource)source).getNode();
        }
        
        //Not a DOM source, transform into a DOM node
        try {
            final TransformerFactory transformerFactory = TransformerFactory.newInstance();
            final Transformer transformer = transformerFactory.newTransformer();
            final DOMResult domResult = new DOMResult();
            transformer.transform(source, domResult);
            return domResult.getNode();
        }
        catch (TransformerConfigurationException tce) {
            //ignore this SoapFaultDetailElement since sadly it could not be converted to a DOM Node
            logger.trace("Failed to configure transformer for Source {} .", source, tce);
            return null;
        }
        catch (TransformerException te) {
            //ignore this SoapFaultDetailElement since sadly it could not be converted to a DOM Node
            logger.trace("Failed to transform Source {} .", source, te);
            return null;
        }
    }
    
    protected String evaluateXPath(Node node, String expression) {
        final XPathFactory xpathFactory = XPathFactory.newInstance();
        final XPath xPath = xpathFactory.newXPath();
        final XPathExpression xpathExpression;
        try {
            xpathExpression = xPath.compile(expression);
        }
        catch (XPathExpressionException e) {
            logger.trace("Failed to compile expression {} .", expression, e);
            return null;
        }
        
        try {
            return xpathExpression.evaluate(node);
        }
        catch (XPathExpressionException e) {
            logger.trace("Failed to evaluate expression {} .", expression, e);
            return null;
        }
    }
}
