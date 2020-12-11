package com.dailystudio.memory.game;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.dailystudio.development.Logger;

public class DesEncrypter {

    public static final int SALT_LENGTH = 20;
    public static final int PBE_ITERATION_COUNT = 200; //1024;

    private static final String PBE_ALGORITHM = "PBEWithSHA256And256BitAES-CBC-BC";

    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";

    byte[] iv = "1234567890asdfgh".getBytes();

    byte[] salt = "dfghjklpoiuytgftgyhj".getBytes();

    public byte[] encrypt(String password, String cleartext) {

        byte[] encryptedText = null;

        try {
            PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, PBE_ITERATION_COUNT, 256);

            SecretKeyFactory factory = SecretKeyFactory.getInstance(PBE_ALGORITHM);

            SecretKey tmp = factory.generateSecret(pbeKeySpec);

            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher encryptionCipher = Cipher.getInstance(CIPHER_ALGORITHM);   

            IvParameterSpec ivspec = new IvParameterSpec(iv);

            encryptionCipher.init(Cipher.ENCRYPT_MODE, secret, ivspec);

            encryptedText = encryptionCipher.doFinal(cleartext.getBytes());
        } catch (Exception e) {
        	Logger.warnning("encrypt failure: %s", e.toString());
        }

        return encryptedText;
    }

    public String decrypt(String password, byte[] encryptedText) {

        String cleartext = "";

        try {
            PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, PBE_ITERATION_COUNT, 256);

            SecretKeyFactory factory = SecretKeyFactory.getInstance(PBE_ALGORITHM);

            SecretKey tmp = factory.generateSecret(pbeKeySpec);

            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher decryptionCipher = Cipher.getInstance(CIPHER_ALGORITHM);

            IvParameterSpec ivspec = new IvParameterSpec(iv);

            decryptionCipher.init(Cipher.DECRYPT_MODE, secret, ivspec);

            byte[] decryptedText = decryptionCipher.doFinal(encryptedText);

            cleartext =  new String(decryptedText); 
        } catch (Exception e) {
        	Logger.warnning("encrypt failure: %s", e.toString());
        }

        return cleartext;
    }      
    
}