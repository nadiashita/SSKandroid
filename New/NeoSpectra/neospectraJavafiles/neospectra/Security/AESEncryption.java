package com.si_ware.neospectra.Security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by AmrWinter on 1/9/18.
 */

public class AESEncryption implements IEncryptDataFiles {
    @Override
    public SecretKey generateKey()
            throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        int KeyLength = 16;
        String password = "AlexandriaUnivMLTeamPassPhrase";
        byte[] AESKey = new byte[KeyLength];
        //Get 16  bytes from the password, AES only supports key sizes of 16, 24 or 32 bytes.
        password.getBytes(0, KeyLength-1, AESKey, 0);
        return new SecretKeySpec(AESKey, "AES");
    }

    @Override
    public String encrypt(String msg, SecretKey key)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            InvalidParameterSpecException, IllegalBlockSizeException, BadPaddingException,
            UnsupportedEncodingException
    {
        /* Encrypt the message. */
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherText = cipher.doFinal(msg.getBytes("UTF-8"));
        return cipherText.toString();
    }

    @Override
    public String decrypt(String cipherData, SecretKey key)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException, UnsupportedEncodingException {
        /* Decrypt the message, given derived encContentValues and initialization vector. */
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] cipherData16Byte = cipherData.getBytes("UTF-8");
        return new String(cipher.doFinal(cipherData16Byte), "UTF-8");
    }
}
