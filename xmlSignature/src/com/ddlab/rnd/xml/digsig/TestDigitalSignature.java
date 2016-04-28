/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddlab.rnd.xml.digsig;

import java.io.File;

/**
 * This class is used as a test class to sign an xml document digitally.
 *
 * @author <a href="mailto:debadatta.mishra@gmail.com">Debadatta Mishra</a>
 * @since 2013
 */
public class TestDigitalSignature {
    /*
     * Main method to generate a digitally signed xml document.
     */
    public static void main(String[] args) {
        String xmlFilePath = "xml" + File.separator + "request.xml";
        String signedXmlFilePath = "xml" + File.separator + "digitallysignedreq.xml";
        
        String keystoreFilePath = "keys" + File.separator + "cacerts";
                
        String privateKeyAlias = "airtel_staging_xml_sig";
        String keyStorePassword = "abc123";
        String keyPassword = "abc123";
        XmlDigitalSignatureGenerator xmlSig = new XmlDigitalSignatureGenerator();
        
        xmlSig.generateXMLDigitalSignature(xmlFilePath, signedXmlFilePath, keystoreFilePath, privateKeyAlias,keyStorePassword.toCharArray(),keyPassword.toCharArray());
    }
}
