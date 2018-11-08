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

package edu.wisc.bnsemail.dao;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.CertStore;
import java.security.cert.Certificate;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.mail.smime.SMIMESignedGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import edu.wisc.hr.dao.bnsemail.BusinessEmailUpdateNotifier;

/**
 * 
 * @author Eric Dalquist
 */
@Service("businessEmailUpdateNotifier")
public class SmtpBusinessEmailUpdateNotifier implements InitializingBean, BusinessEmailUpdateNotifier {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    private SMIMESignedGenerator smimeSignedGenerator;
    
    private JavaMailSender javaMailSender;
    private Resource keystore;
    private String keystorePassword;
    private String certificateAlias;
    
    public void setKeystore(Resource keystore) {
        this.keystore = keystore;
    }

    public void setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
    }

    public void setCertificateAlias(String certificateAlias) {
        this.certificateAlias = certificateAlias;
    }

    @Autowired
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.keystore == null) {
            this.logger.warn("No S/MIME KeyStore configured. Email update notifications will NOT be signed");
        }
        else {
            Security.addProvider(new BouncyCastleProvider());
    
            final KeyStore signingKeyStore = KeyStore.getInstance("JKS");
            
            final InputStream keyStoreStream = this.keystore.getInputStream();
            try {
                signingKeyStore.load(keyStoreStream, this.keystorePassword.toCharArray());
            }
            finally {
                IOUtils.closeQuietly(keyStoreStream);
            }
            
            final List<Certificate> certList = new ArrayList<Certificate>(1);
            for (final Enumeration<String> aliasesEnum = signingKeyStore.aliases(); aliasesEnum.hasMoreElements();) {
                final String alias = aliasesEnum.nextElement();
                final Certificate cert = signingKeyStore.getCertificate(alias);
                if (cert != null) {
                    certList.add(cert);
                }
            }
            
            final PrivateKey signingKey = (PrivateKey) signingKeyStore.getKey(this.certificateAlias, this.keystorePassword.toCharArray());
            final X509Certificate signingCert = (X509Certificate) signingKeyStore.getCertificate(this.certificateAlias);
            
            // create a CertStore containing the certificates we want carried
            // in the signature
            final CertStore certsAndcrls = CertStore.getInstance("Collection", new CollectionCertStoreParameters(certList), "BC");
    
            // create the generator for creating an smime/signed message
            smimeSignedGenerator = new SMIMESignedGenerator();
    
            // add a signer to the generator - this specifies we are using SHA1 and
            // adding the smime attributes above to the signed attributes that
            // will be generated as part of the signature. The encryption algorithm
            // used is taken from the key - in this RSA with PKCS1Padding
            smimeSignedGenerator.addSigner(signingKey, signingCert, SMIMESignedGenerator.DIGEST_SHA1);
    
            // add our pool of certs and cerls (if any) to go with the signature
            smimeSignedGenerator.addCertificatesAndCRLs(certsAndcrls);
        }
    }

    /* (non-Javadoc)
     * @see edu.wisc.bnsemail.dao.BusinessEmailUpdateNotifier#notifyEmailUpdated(java.lang.String, java.lang.String)
     */
    @Override
    public void notifyEmailUpdated(String oldAddress, String newAddress) {
        try {
            //Create the message body
            final MimeBodyPart msg = new MimeBodyPart();
            msg.setContent(
                    "Your Business Email Address has changed\n" + 
                    "\n" + 
                    "Old Email Address: " + oldAddress + "\n" + 
                    "New Email Address: " + newAddress + "\n" + 
                    "\n" + 
                    "If you have any questions, please contact your Human Resources department.", "text/plain");
    
            final MimeMessage message = this.javaMailSender.createMimeMessage();
            final Address[] recipients;
            if (StringUtils.isNotEmpty(oldAddress)) {
            	recipients = new Address[] { new InternetAddress(oldAddress), new InternetAddress(newAddress) };
            }
            else {
            	recipients = new Address[] { new InternetAddress(newAddress) };
            }
			message.setRecipients(RecipientType.TO, recipients);
            message.setFrom(new InternetAddress("payroll@ohr.wisc.edu"));
            message.setSubject("Business Email Address Change");
            
            // sign the message body
            if (this.smimeSignedGenerator != null) {
                final MimeMultipart mm = this.smimeSignedGenerator.generate(msg, "BC");
                message.setContent(mm, mm.getContentType());
            }
            // no signing keystore configured, send the message unsigned
            else {
                message.setContent(msg.getContent(), msg.getContentType());
            }
            
            message.saveChanges();
    
            this.javaMailSender.send(message);
            
            this.logger.info("Sent notification of email address change from {} to {}", oldAddress, newAddress);
        }
        catch (Exception e) {
            this.logger.error("Failed to send notification email for change from " + oldAddress + " to " + newAddress , e);
        }
    }
}
