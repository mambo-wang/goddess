package com.h3c.vdi.athena.common.utils;

import com.h3c.vdi.athena.common.exception.AppException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author w14014
 * @date 2018/10/18
 */
public class MD5Util {

    /** 十六进制下数字到字符的映射数组 */
    private final static String[] HEX_DIGITS = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};

    /** 把inputStr加密 */
    public static String md5(String inputStr){
        return encodeByMD5(inputStr);
    }

    /**
     * 验证输入的密码是否正确
     * @param encoded 真正的密码（加密后）
     * @param inputString 输入的字符串
     * @return 验证结果
     */
    public static boolean authenticatePassword(String encoded, String inputString){
        if(encoded.equals(encodeByMD5(inputString))){
            return true;
        } else {
            return false;
        }
    }

    /**
     * 对字符串进行MD5编码
     * @param originString 原始字符串
     * @return 加密后的字符串
     */
    private static String encodeByMD5(String originString){
        try{
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] results = md5.digest(originString.getBytes());
            String result = byteArrayToHexString(results);
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new AppException("未找到MD5加密方式");
        }
    }

    /**
     * 转换字节数组为十六进制字符串
     * @param b 字节数组
     * @return 十六进制字符串
     */
    private static String byteArrayToHexString(byte[] b){
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < b.length; i++){
            sb.append(byteToHexString(b[i]));
        }
        return sb.toString();
    }

    /**
     * 将一个字节转换为十六进制形式的字符串
     * @param b 字节
     * @return 字符串
     */
    private static String byteToHexString(byte b){
        int n = b;
        if(n < 0){
            n = 256 + n;
        }
        int d1 = n/16;
        int d2 = n%16;

        return HEX_DIGITS[d1] + HEX_DIGITS[d2];
    }

}
