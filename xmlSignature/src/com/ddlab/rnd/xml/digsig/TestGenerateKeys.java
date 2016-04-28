/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ddlab.rnd.xml.digsig;

import com.ddlab.rnd.crypto.KryptoUtil;

/**
 * This class is used as a test class to generate the cryptographic keys ie
 * public and private key.
 *
 * @author <a href="mailto:debadatta.mishra@gmail.com">Debadatta Mishra</a>
 * @since 2013
 */
public class TestGenerateKeys {
    /*
     * Main method to generate the keys
     */
    public static void main(String[] args) {
        String keysDirPath = "keys";
        KryptoUtil util = new KryptoUtil();
        util.storeKeyPairs(keysDirPath);
        System.out.println("Private and Public Keys generated successfully ...");
    }
}
