package com.wfcrc.utils;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;

import android.util.Log;

public class SecurityUtils {

    public static final String ENCRYPTION_ALGORITHM_AES = "DESede";

    private SecretKeyFactory keyFactory;

    private SecretKey key;

    private SecureRandom secureRandom;

    private String algorithm;

    private static final byte[] salt = { (byte) 0x4F, (byte) 0xA0, (byte) 0x04, (byte) 0x87, (byte) 0x21, (byte) 0xC5, (byte) 0x6D, (byte) 0xCC };

    public SecurityUtils(char[] pwd) {
        try {
            setAlgorithm(ENCRYPTION_ALGORITHM_AES);
            keyFactory = SecretKeyFactory.getInstance(getAlgorithm(), "BC");
            setPassword(pwd);
            secureRandom = new SecureRandom();
        } catch (NoSuchAlgorithmException e) {
            Log.d("WFCRC", "Error when creating Key Factory.", e);
        } catch (NoSuchProviderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void setPassword(char[] pwd) {
        key = null;
        if (pwd != null) {
            if (ENCRYPTION_ALGORITHM_AES.equals(algorithm)) {
                // 3DES
                try {
                    // key = keyFactory.generateSecret(new DESedeKeySpec(hexStringToBytes(new String(pwd))));
                    byte[] aux = new String(pwd).getBytes();
                    byte[] aux2 = new byte[24];
                    if (aux.length < 24) {
                        for (int i = 0; i < aux.length; i++) {
                            aux2[i] = aux[i];
                        }
                        for (int i = aux.length; i < aux2.length; i++) {
                            aux2[i] = 0x00;
                        }
                        key = keyFactory.generateSecret(new DESedeKeySpec(aux2));
                    } else {
                        key = keyFactory.generateSecret(new DESedeKeySpec(aux));
                    }
                } catch (InvalidKeyException e) {

                } catch (Exception e) {
                }
            }
        }
    }

    public boolean checkPassword(char[] pwd, byte[] providedHash) {
        boolean equal = false;
        try {
            SecretKey aesKey = keyFactory.generateSecret(new PBEKeySpec(pwd));
            byte[] hashAesKey = aesKey.getEncoded();
            equal = Arrays.equals(providedHash, hashAesKey);
        } catch (InvalidKeySpecException e) {
            Log.d("WFCRC", "Error when generating password.", e);
        }
        return equal;
    }

    public static String bytesToHexString(byte[] bytes) throws Exception {
        String result = null;
        if (bytes != null) {
            try {
                result = new String(Hex.encode(bytes));
            } catch (Exception e) {
                Log.d("WFCRC", "Error when encoding message.", e);
                throw new Exception();
            }
        }
        return result;
    }

    public static byte[] hexStringToBytes(String hexString) throws Exception {
        byte[] result = null;
        if (hexString != null) {
            try {
                result = (byte[]) Hex.decode(hexString);
            } catch (Exception e) {
                Log.d("WFCRC", "Error when decoding message.", e);
                throw new Exception();
            }
        }
        return result;
    }

    public byte[] encrypt(byte[] data) throws Exception {
        byte[] encryptedBytes = data;

        if (key != null && data != null) {
            try {
                Cipher cipher = Cipher.getInstance(getAlgorithm(), "BC");
                if (ENCRYPTION_ALGORITHM_AES.equals(algorithm)) {
                    cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(salt));
                }
                encryptedBytes = cipher.doFinal(data);
            } catch (NoSuchAlgorithmException e) {
                Log.d("WFCRC", "Error generating cipher.", e);
                throw new Exception();
            } catch (NoSuchPaddingException e) {
                Log.d("WFCRC", "Error generating cipher.", e);
                throw new Exception();
            } catch (InvalidKeyException e) {
                Log.d("WFCRC", "Error encrypting text.", e);
                throw new Exception();
            } catch (IllegalBlockSizeException e) {
                Log.d("WFCRC", "Error encrypting text.", e);
                throw new Exception();
            } catch (BadPaddingException e) {
                Log.d("WFCRC", "Error encrypting text.", e);
                throw new Exception();
            } catch (InvalidAlgorithmParameterException e) {
                Log.d("WFCRC", "Error encrypting text.", e);
                throw new Exception();
            }
        }
        return encryptedBytes;

    }

    public String encrypt(String text) throws Exception {
        String encryptedText = null;
        if (text != null) {
            encryptedText = bytesToHexString(encrypt(text.getBytes()));
        }
        return encryptedText;
    }

    public byte[] decrypt(byte[] data) throws Exception {
        byte[] plainTextBytes = data;

        if (key != null && data != null) {
            try {
                Cipher cipher = Cipher.getInstance(getAlgorithm(), "BC");
                if (ENCRYPTION_ALGORITHM_AES.equals(algorithm)) {
                    cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(salt));
                }
                plainTextBytes = cipher.doFinal(data);
            } catch (NoSuchAlgorithmException e) {
                Log.d("WFCRC", "Error generating cipher.", e);
                throw new Exception();
            } catch (NoSuchPaddingException e) {
                Log.d("WFCRC", "Error generating cipher.", e);
                throw new Exception();
            } catch (InvalidKeyException e) {
                Log.d("WFCRC", "Error decrypting text.", e);
                throw new Exception();
            } catch (IllegalBlockSizeException e) {
                Log.d("WFCRC", "Error decrypting text.", e);
                throw new Exception();
            } catch (BadPaddingException e) {
                Log.d("WFCRC", "Error decrypting text.", e);
                throw new Exception();
            } catch (InvalidAlgorithmParameterException e) {
                Log.d("WFCRC", "Error decrypting text.", e);
                throw new Exception();
            }
        }
        return plainTextBytes;
    }

    public String decrypt(String encryptedText) throws Exception {
        String plainText = null;
        if (encryptedText != null) {
            plainText = new String(decrypt(hexStringToBytes(encryptedText)));
        }
        return plainText;
    }

    public SecureRandom getSecureRandom() {
        return secureRandom;
    }

    public void setSecureRandom(SecureRandom secureRandom) {
        this.secureRandom = secureRandom;
    }

    /**
     * Returns the algorithm property.
     *
     * @return String
     */
    public String getAlgorithm() {
        return algorithm;
    }

    /**
     * Sets the algorithm property.
     *
     * @param algorithm
     *            The algorithm to set.
     */
    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public byte[] generateRandomKey(int length) {
        Random rnd = new Random();
        byte[] key = null;
        boolean exactSize = false;
        do {
            BigInteger rndInteger = new BigInteger(length, rnd);
            key = rndInteger.toByteArray();
            if (key.length == (length / 8)) {
                exactSize = true;
            }
        } while (!exactSize);
        return key;
    }

}
