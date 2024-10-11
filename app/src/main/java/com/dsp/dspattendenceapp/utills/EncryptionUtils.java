package com.dsp.dspattendenceapp.utills;

import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;


import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.spec.KeySpec;
import java.security.AlgorithmParameters;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.CipherOutputStream;
import java.io.ByteArrayOutputStream;
//import java.util.Base64;

import android.util.Base64;
public class EncryptionUtils extends AsyncTask<String, Void, String> {


    private EncryptionCallback callback;

    public EncryptionUtils(EncryptionCallback callback) {
        this.callback = callback;
    }

    private static final String ENCRYPTION_KEY = "MAKV2SPBNI99212";
   // private static final byte[] SALT = { 0x49, 0x76, 0x61, 0x6E, 0x20, 0x4D, 0x65, 0x64, 0x76, 0x65, 0x64, 0x65, 0x76 };



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected String doInBackground(String... strings) {
        try {
            // Call the encrypt method in the background
            return encryptUrlParams(strings[0],"","");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        // Handle the encrypted result (update the UI if necessary)
        if (callback != null) {
            callback.onEncryptionComplete(result);
        }
    }

//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public static String encrypt(String clearText) throws Exception {
//        byte[] clearBytes = clearText.getBytes(StandardCharsets.UTF_16LE);
//        SecretKeySpec secretKey = generateSecretKey(ENCRYPTION_KEY, SALT);
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
//        AlgorithmParameters params = cipher.getParameters();
//        byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
//        byte[] encryptedBytes = cipher.doFinal(clearBytes);
//
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        outputStream.write(iv);
//        outputStream.write(encryptedBytes);
//        byte[] combinedBytes = outputStream.toByteArray();
//        return Base64.encodeToString(combinedBytes, Base64.DEFAULT);
//    }
//
//    private static SecretKeySpec generateSecretKey(String password, byte[] salt) throws Exception {
//        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
//        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 500, 256);
//        SecretKey tmp = factory.generateSecret(spec);
//        return new SecretKeySpec(tmp.getEncoded(), "AES");
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public static String decrypt(String cipherText) throws Exception {
//        byte[] cipherBytes = Base64.decode(cipherText,Base64.DEFAULT);
//        SecretKeySpec secretKey = generateSecretKey(ENCRYPTION_KEY, SALT);
//        byte[] iv = new byte[16];
//        System.arraycopy(cipherBytes, 0, iv, 0, iv.length);
//        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
//        byte[] decryptedBytes = cipher.doFinal(cipherBytes, 16, cipherBytes.length - 16);
//        return new String(decryptedBytes, StandardCharsets.UTF_16LE);
//    }
public static String encryptUrlParams(String empid, String pass, String dt) {
    try {
        // Concatenate the parameters
        String clearText = empid + "|" + pass + "|" + dt;

        // Convert clear text and key to byte arrays
        byte[] clearBytes = clearText.getBytes(StandardCharsets.UTF_8);
        byte[] keyBytes = ENCRYPTION_KEY.getBytes(StandardCharsets.UTF_8);

        // Perform XOR encryption
        for (int i = 0; i < clearBytes.length; i++) {
            clearBytes[i] ^= keyBytes[i % keyBytes.length]; // XOR each byte with the key
        }

        // Base64 encode the encrypted bytes
        String base64Encoded = Base64.encodeToString(clearBytes, Base64.NO_WRAP);

        // URL encode the result
        return URLEncoder.encode(base64Encoded, "UTF-8");

    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}

    public static String decryptUrlParams(String encryptedText) {
        try {
            // URL decode the input
            String urlDecodedText = URLDecoder.decode(encryptedText, "UTF-8");

            // Base64 decode the input
            byte[] cipherBytes = Base64.decode(urlDecodedText, Base64.NO_WRAP);
            byte[] keyBytes = ENCRYPTION_KEY.getBytes(StandardCharsets.UTF_8);

            // Perform XOR decryption
            for (int i = 0; i < cipherBytes.length; i++) {
                cipherBytes[i] ^= keyBytes[i % keyBytes.length];
            }

            return new String(cipherBytes, StandardCharsets.UTF_8);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public interface EncryptionCallback {
        void onEncryptionComplete(String result);
    }
}
