package edu.wisc.hrs.dao;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.FaultMessageResolver;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.client.SoapFaultClientException;

public class ResponseLoggingFaultResolver
  implements FaultMessageResolver {

  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  public void resolveFault(WebServiceMessage message) throws IOException {

    if (logger.isDebugEnabled()) {

      ByteArrayOutputStream messageAsOutputStream = new ByteArrayOutputStream();

      message.writeTo(messageAsOutputStream);

      String messageAsString = messageAsOutputStream.toString();

      logger.debug("Error response: [" + messageAsString + "]");
    }

    // do what SoapFaultMessageResolver does
    SoapMessage soapMessage = (SoapMessage) message;
    throw new SoapFaultClientException(soapMessage);
  }
}
