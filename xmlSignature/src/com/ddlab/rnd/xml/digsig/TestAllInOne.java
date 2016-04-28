/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddlab.rnd.xml.digsig;

import com.ddlab.rnd.crypto.KryptoUtil;
import java.io.File;

/**
 * This is a test class to show how XML digital signature works. It performs the
 * following functionalities 1. Generates Private and Public Keys 2. Using
 * Private and Public Keys, sign the document 3. Verify the signed XML document
 * using public key
 *
 * @author <a href="mailto:debadatta.mishra@gmail.com">Debadatta Mishra</a>
 * @since 2013
 */
public class TestAllInOne {

    public static void main(String[] args) {
        //Generate the keys
        String keysDirPath = "keys";
        KryptoUtil util = new KryptoUtil();
        util.storeKeyPairs(keysDirPath);
        System.out.println("Private and Public Keys generated successfully ...");
        //Sign the XML Dcouments
        String xmlFilePath = "xml" + File.separator + "employeesalary.xml";
        String signedXmlFilePath = "xml" + File.separator + "digitallysignedEmpSal.xml";
        String privateKeyFilePath = "keys" + File.separator + "privatekey.key";
        String publicKeyFilePath = "keys" + File.separator + "publickey.key";
        XmlDigitalSignatureGenerator xmlSig = new XmlDigitalSignatureGenerator();
        xmlSig.generateXMLDigitalSignature(xmlFilePath, signedXmlFilePath, privateKeyFilePath, publicKeyFilePath);
        //Verify the signed XML Document
        try {
            boolean validFlag = XmlDigitalSignatureVerifier.
                    isXmlDigitalSignatureValid(signedXmlFilePath, publicKeyFilePath);
            System.out.println("Validity of XML Digital Signature : " + validFlag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
