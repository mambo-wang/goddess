package com.h3c.vdi.athena.common.utils;

/**
 * Created by w14014 on 2018/4/10.
 */
import java.nio.charset.Charset;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EncryptUtils {
    private static Charset CHARSET_UTF8 = Charset.forName("UTF-8");
    private static Log log = LogFactory.getLog(EncryptUtils.class);
    private static final byte[] PASSWORD_KEY = new byte[]{108, 105, 95, 48, 49, 48, 49, 48};
    private static Cipher encryptDesCipher;
    private static Cipher decryptDesCipher;

    public EncryptUtils() {
    }

    public static String encryptText(String plainText) {
        try {
            byte[] e = encryptDesCipher.doFinal(plainText.getBytes(CHARSET_UTF8));
            return e != null?new String(Base64.encodeBase64(e), CHARSET_UTF8):null;
        } catch (Exception var2) {
            log.warn((Object)null, var2);
            return null;
        }
    }

    public static String decryptText(String cryptoText) {
        try {
            byte[] e = Base64.decodeBase64(cryptoText.getBytes(CHARSET_UTF8));
            if(e == null) {
                return null;
            } else {
                byte[] plainText = decryptDesCipher.doFinal(e);
                return plainText != null?new String(plainText, CHARSET_UTF8):null;
            }
        } catch (Exception var3) {
            log.warn((Object)null, var3);
            return null;
        }
    }

    static {
        DESKeySpec e;
        SecretKeyFactory factory;
        SecretKey desSecretKey;
        try {
            e = new DESKeySpec(PASSWORD_KEY);
            factory = SecretKeyFactory.getInstance("DES");
            desSecretKey = factory.generateSecret(e);
            encryptDesCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            encryptDesCipher.init(1, desSecretKey);
        } catch (Throwable var4) {
            log.warn((Object)null, var4);
        }

        try {
            e = new DESKeySpec(PASSWORD_KEY);
            factory = SecretKeyFactory.getInstance("DES");
            desSecretKey = factory.generateSecret(e);
            decryptDesCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            decryptDesCipher.init(2, desSecretKey);
        } catch (Throwable var3) {
            log.warn((Object)null, var3);
        }

    }
}
