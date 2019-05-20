package com.goodsogood.ows.helper;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

public class DESUtilGSG {
    private static final String DES_ALGORITHM = "DES";

    public static String encryption(String plainData, String secretKey) throws Exception {
        try {
            final Cipher cipher = Cipher.getInstance(DES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, generateKey(secretKey));

            //为了防止解密时报javax.crypto.IllegalBlockSizeException:Input length must
            // be multiple of 8 when decrypting with padded cipher异常，
            // 不能把加密后的字节数组直接转换成字符串
            byte[] buf = cipher.doFinal(plainData.getBytes());
            return Base64.getEncoder().encodeToString(buf);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw e;
        } catch (IllegalBlockSizeException e) {
            throw new Exception("IllegalBlockSizeException", e);
        } catch (BadPaddingException e) {
            throw new Exception("BadPaddingException", e);
        }
    }

    public static String decryption(String secretData, String secretKey) throws Exception {

        try {
            final Cipher cipher = Cipher.getInstance(DES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, generateKey(secretKey));
            byte[] buf = cipher.doFinal(Base64.getDecoder().decode(secretData.getBytes()));
            return new String(buf);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            throw e;
        } catch (IllegalBlockSizeException e) {
            throw new Exception("IllegalBlockSizeException", e);
        } catch (BadPaddingException e) {
            throw new Exception("BadPaddingException", e);
        }
    }

    private static SecretKey generateKey(String secretKey)
            throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES_ALGORITHM);
        DESKeySpec keySpec = new DESKeySpec(secretKey.getBytes());
        keyFactory.generateSecret(keySpec);
        return keyFactory.generateSecret(keySpec);
    }
}
