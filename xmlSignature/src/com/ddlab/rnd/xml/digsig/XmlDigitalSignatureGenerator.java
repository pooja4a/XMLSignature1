package com.ddlab.rnd.xml.digsig;

import com.ddlab.rnd.crypto.KryptoUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.security.cert.X509Certificate;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.keyinfo.KeyInfoFactory;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.keyinfo.X509Data;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * This class is used to provide convenient methods to digitally sign an XML
 * document.
 *
 * @author <a href="mailto:debadatta.mishra@gmail.com">Debadatta Mishra</a>
 * @since 2013
 */
public class XmlDigitalSignatureGenerator {

    /**
     * Method used to get the XML document by parsing
     *
     * @param xmlFilePath , file path of the XML document
     * @return Document
     */
    private Document getXmlDocument(String xmlFilePath) {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        try {
            doc = dbf.newDocumentBuilder().parse(new FileInputStream(xmlFilePath));
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return doc;
    }

   

    private KeyInfo getKeyInfo( XMLSignatureFactory fac,X509Certificate cert) {
		// Create the KeyInfo containing the X509Data.
		KeyInfoFactory kif = fac.getKeyInfoFactory();
		List x509Content = new ArrayList();
		x509Content.add(cert.getSubjectX500Principal().getName());
		x509Content.add(cert);
		X509Data xd = kif.newX509Data(x509Content);
		return kif.newKeyInfo(Collections.singletonList(xd));
	}

    
    
    /*
     * Method used to store the signed XMl document
     */
    private void storeSignedDoc(Document doc, String destnSignedXmlFilePath) {
        TransformerFactory transFactory = TransformerFactory.newInstance();
        Transformer trans = null;
        try {
            trans = transFactory.newTransformer();
        } catch (TransformerConfigurationException ex) {
            ex.printStackTrace();
        }
        try {
            StreamResult streamRes = new StreamResult(new File(destnSignedXmlFilePath));
            trans.transform(new DOMSource(doc), streamRes);
        } catch (TransformerException ex) {
            ex.printStackTrace();
        }
        System.out.println("XML file with attached digital signature generated successfully ...");
    }

    /**
     * Method used to attach a generated digital signature to the existing
     * document
     *
     * @param originalXmlFilePath
     * @param destnSignedXmlFilePath
     * @param privateKeyFilePath
     * @param publicKeyFilePath
     */
    public void generateXMLDigitalSignature(String originalXmlFilePath,
            String destnSignedXmlFilePath, String privateKeyFilePath, String alias,char [] keyStorePassword, char [] keyPassword ) {
        //Get the XML Document object
//    	char[] keyStorePassword = "airtel".toCharArray();
//    	char[] keyPassword = "airtel".toCharArray();
//    	String alias = "volt_ivr_key";
        Document doc = getXmlDocument(originalXmlFilePath);
        //Create XML Signature Factory
        XMLSignatureFactory xmlSigFactory = XMLSignatureFactory.getInstance("DOM");
        KryptoUtil ku = new KryptoUtil(privateKeyFilePath,keyStorePassword);
        PrivateKey privateKey = ku.getStoredPrivateKey(keyPassword,alias);
        DOMSignContext domSignCtx = new DOMSignContext(privateKey, doc.getDocumentElement());
        //DOMSignContext domSignCtx = new DOMSignContext(privateKey, doc.getElementsByTagName(null).item(0));
        
        Reference ref = null;
        SignedInfo signedInfo = null;
        try {
            ref = xmlSigFactory.newReference("", xmlSigFactory.newDigestMethod(DigestMethod.SHA1, null),
                    Collections.singletonList(xmlSigFactory.newTransform(Transform.ENVELOPED,
                    (TransformParameterSpec) null)), null, null);
            signedInfo = xmlSigFactory.newSignedInfo(
                    xmlSigFactory.newCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE,
                    (C14NMethodParameterSpec) null),
                    xmlSigFactory.newSignatureMethod(SignatureMethod.RSA_SHA1, null),
                    Collections.singletonList(ref));
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        } catch (InvalidAlgorithmParameterException ex) {
            ex.printStackTrace();
        }
        //Pass the Public Key File Path 
        KeyInfo keyInfo = getKeyInfo(xmlSigFactory, ku.getCertificate());
        //Create a new XML Signature
        XMLSignature xmlSignature = xmlSigFactory.newXMLSignature(signedInfo, null);
        try {
            //Sign the document
            xmlSignature.sign(domSignCtx);
        } catch (MarshalException ex) {
            ex.printStackTrace();
        } catch (XMLSignatureException ex) {
            ex.printStackTrace();
        }
        //Store the digitally signed document inta a location
        storeSignedDoc(doc, destnSignedXmlFilePath);
    }
}
