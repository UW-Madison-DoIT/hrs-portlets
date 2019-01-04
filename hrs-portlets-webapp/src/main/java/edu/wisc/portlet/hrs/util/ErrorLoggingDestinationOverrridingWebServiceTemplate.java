package edu.wisc.portlet.hrs.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.xml.transform.TransformerException;
import org.jasig.springframework.ws.client.core.DestinationOverridingWebServiceTemplate;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.WebServiceTransformerException;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceMessageExtractor;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.transport.WebServiceConnection;

/**
 * Extends superclass attempting to log the raw response at TRACE level when erring while sending
 * and receiving a web service message.
 */
public class ErrorLoggingDestinationOverrridingWebServiceTemplate
  extends DestinationOverridingWebServiceTemplate  {

  /*
   * Implementation copied from super-class.
   */
  @SuppressWarnings("unchecked")
  @Override
  protected <T> T doSendAndReceive(MessageContext messageContext,
      WebServiceConnection connection,
      WebServiceMessageCallback requestCallback,
      WebServiceMessageExtractor<T> responseExtractor) throws IOException {
    try {
      if (requestCallback != null) {
        requestCallback.doWithMessage(messageContext.getRequest());
      }
      // Apply handleRequest of registered interceptors
      int interceptorIndex = -1;

      ClientInterceptor[] interceptors = getInterceptors();

      if (interceptors != null) {
        for (int i = 0; i < interceptors.length; i++) {
          interceptorIndex = i;
          if (!interceptors[i].handleRequest(messageContext)) {
            break;
          }
        }
      }
      // if an interceptor has set a response, we don't send/receive
      if (!messageContext.hasResponse()) {
        sendRequest(connection, messageContext.getRequest());
        if (hasError(connection, messageContext.getRequest())) {

          // MODIFIED: attempt to get and log the response
          if (logger.isDebugEnabled()) { // logResponse is a no-op above DEBUG logging
            try {
              WebServiceMessage response = connection.receive(getMessageFactory());
              messageContext.setResponse(response);
              logResponse(messageContext);
            } catch (Exception e) {
              logger.warn("In error case. Failed to log response.", e);
            }
          }

          return (T)handleError(connection, messageContext.getRequest());
        }
        WebServiceMessage response = connection.receive(getMessageFactory());
        messageContext.setResponse(response);
      }
      logResponse(messageContext);
      if (messageContext.hasResponse()) {
        if (!hasFault(connection, messageContext.getResponse())) {
          triggerHandleResponse(interceptorIndex, messageContext);
          return responseExtractor.extractData(messageContext.getResponse());
        }
        else {
          triggerHandleFault(interceptorIndex, messageContext);
          return (T)handleFault(connection, messageContext);
        }
      }
      else {
        return null;
      }
    }
    catch (TransformerException ex) {
      throw new WebServiceTransformerException("Transformation error: " + ex.getMessage(), ex);
    }
  }

  /*
   * Copied from superclass to make private method available to this sub-class.
   */
  private void sendRequest(WebServiceConnection connection, WebServiceMessage request) throws IOException {
    if (sentMessageTracingLogger.isTraceEnabled()) {
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      request.writeTo(os);
      sentMessageTracingLogger.trace("Sent request [" + os.toString("UTF-8") + "]");
    }
    else if (sentMessageTracingLogger.isDebugEnabled()) {
      sentMessageTracingLogger.debug("Sent request [" + request + "]");
    }
    connection.send(request);
  }

  /*
   * Copied from superclass to make private method available to this sub-class.
   */
  private void logResponse(MessageContext messageContext) throws IOException {
    if (messageContext.hasResponse()) {
      if (receivedMessageTracingLogger.isTraceEnabled()) {
        ByteArrayOutputStream requestStream = new ByteArrayOutputStream();
        messageContext.getRequest().writeTo(requestStream);
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
        messageContext.getResponse().writeTo(responseStream);
        receivedMessageTracingLogger
            .trace("Received response [" + responseStream.toString("UTF-8") + "] for request [" +
                requestStream.toString("UTF-8") + "]");
      }
      else if (receivedMessageTracingLogger.isDebugEnabled()) {
        receivedMessageTracingLogger
            .debug("Received response [" + messageContext.getResponse() + "] for request [" +
                messageContext.getRequest() + "]");
      }
    }
    else {
      if (logger.isDebugEnabled()) {
        receivedMessageTracingLogger
            .debug("Received no response for request [" + messageContext.getRequest() + "]");
      }
    }
  }

  /*
   * Copied from superclass to make private method available to this sub-class.
   * Modified to access interceptors via accessor method.
   */
  private void triggerHandleResponse(int interceptorIndex, MessageContext messageContext) {
    ClientInterceptor[] interceptors = getInterceptors();
    if (messageContext.hasResponse() && interceptors != null) {
      for (int i = interceptorIndex; i >= 0; i--) {
        if (!interceptors[i].handleResponse(messageContext)) {
          break;
        }
      }
    }
  }

  /*
   * Copied from superclass to make private method available to this sub-class.
   * Modified to access interceptors via accessor method.
   */
  private void triggerHandleFault(int interceptorIndex, MessageContext messageContext) {
    ClientInterceptor[] interceptors = getInterceptors();
    if (messageContext.hasResponse() && interceptors != null) {
      for (int i = interceptorIndex; i >= 0; i--) {
        if (!interceptors[i].handleFault(messageContext)) {
          break;
        }
      }
    }
  }

}
