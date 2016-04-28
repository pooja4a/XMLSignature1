package com.ddlab.rnd.crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore.Entry;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.KeyStore;

import java.security.cert.X509Certificate;
//import org.bouncycastle.jce.provider.BouncyCastleProvider;
/**
 * This class is used as a cryptographic utility.
 *
 * @author <a href="mailto:debadatta.mishra@gmail.com">Debadatta Mishra</a>
 * @since 2013
 */
public class KryptoUtil {

    /**
     * Name of the algorithm
     */
    private static final String ALGORITHM = "RSA";
    private static final String MEC_TYPE = "DOM";
	private static final String WHOLE_DOC_URI = "";
	private static final String KEY_STORE_TYPE = "JKS";
	
	private KeyStore.PrivateKeyEntry keyEntry;
	
	
	static KeyStore ks = null;
	
	
	
	public KryptoUtil(String keyStoreFile, char[] keyStorePassword)
	{
		
				
		try {
			ks = KeyStore.getInstance(KEY_STORE_TYPE);
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	       
        File file = new File(keyStoreFile);
		FileInputStream is = null;
 	
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		        
        try {
			ks.load(is, keyStorePassword);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //this.keyStorePassword = keyStorePassword;
	
	}

    public KryptoUtil() {
		// TODO Auto-generated constructor stub
	}

	/**
     * This method is used to generate key pair based upon the provided
     * algorithm
     *
     * @return KeyPair
     */
    private KeyPair generateKeyPairs() {
        KeyPair keyPair = null;
        KeyPairGenerator keyGen;
        try {
            keyGen = KeyPairGenerator.getInstance(ALGORITHM);
            keyGen.initialize(1024);
            keyPair = keyGen.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return keyPair;
    }

    /**
     * Method used to store Private and Public Keys inside a directory
     *
     * @param dirPath to store the keys
     */
    public void storeKeyPairs(String dirPath) {
        KeyPair keyPair = generateKeyPairs();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        storeKeys(dirPath + File.separator + "publickey.key", publicKey);
        storeKeys(dirPath + File.separator + "privatekey.key", privateKey);
    }

    /**
     * Method used to store the key(Public/Private)
     *
     * @param filePath , name of the file
     * @param key
     */
    private void storeKeys(String filePath, Key key) {
        byte[] keyBytes = key.getEncoded();
        OutputStream outStream = null;
        try {
            outStream = new FileOutputStream(filePath);
            outStream.write(keyBytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Method used to retrieve the keys in the form byte array
     *
     * @param filePath of the key
     */
    private byte[] getKeyData(String filePath) {
        File file = new File(filePath);
        byte[] buffer = new byte[(int) file.length()];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            fis.read(buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer;
    }

    /**
     * Method used to get the generated Private Key
     *
     * @param filePath of the PrivateKey file
     * @return PrivateKey
     */
    public PrivateKey getStoredPrivateKey(char[] keyPassword,String alias) {
    	    	
    	
    		// Load the KeyStore and get the signing key and certificate.
    		
    		try {
    			keyEntry = (KeyStore.PrivateKeyEntry)ks.getEntry(alias,
    					new KeyStore.PasswordProtection(keyPassword));
       			
    			return keyEntry.getPrivateKey();
    			
    			
    		} catch (Exception e) {
    			e.printStackTrace();
    			return null;
    		} finally {
    		
    		}

    	}
    	
    public X509Certificate getCertificate()
    {
    	X509Certificate x509Cert = (X509Certificate) keyEntry.getCertificate();
    	
    	return x509Cert;
    }

    /**
     * Method used to get the generated Public Key
     *
     * @param filePath of the PublicKey file
     * @return PublicKey
     */
    public PublicKey getStoredPublicKey(String certificateFile) throws GeneralSecurityException, IOException{
        PublicKey publicKey = null;
        
        
			
			FileInputStream fis = null;
			CertificateFactory certFactory = null;

			try {
				
				
					Provider [] listProviders = Security.getProviders();
					certFactory = CertificateFactory.getInstance("X.509");
					fis = new FileInputStream(certificateFile);
					
					
					return certFactory.generateCertificate(fis).getPublicKey();
					
				
				
			} finally {
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}

    
}
