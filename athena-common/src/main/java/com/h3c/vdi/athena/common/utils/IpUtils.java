package com.h3c.vdi.athena.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * ip、子网掩码校验
 */
public class IpUtils {

    private static Logger log = LoggerFactory.getLogger(IpUtils.class);

    public static BigInteger convertHostIpToBigInt(String hostIp) {
        if (isValidIpv4(hostIp)) {
            return convertHostIpv4ToBigInt(hostIp);
        }
        return convertHostIpv6ToBigInt(hostIp);
    }

    private static BigInteger convertHostIpv4ToBigInt(String hostIp) {
        long ip1 = 0L;
        long ip2 = 0L;

        if (null == hostIp) {
            return BigInteger.ZERO;
        }

        StringTokenizer st = new StringTokenizer(hostIp, ".");
        if (st.countTokens() != 4) {
            return BigInteger.ZERO;
        }

        try {
            while (st.hasMoreTokens()) {
                ip2 = Integer.parseInt(st.nextToken());

                if ((ip2 < 0L) || (ip2 > 255L)) {
                    return BigInteger.ZERO;
                }
                ip1 = ip1 << 8 | ip2;
            }
        } catch (NumberFormatException e) {
            log.warn(null, e);
            return BigInteger.ZERO;
        }

        return BigInteger.valueOf(ip1);
    }

    public static String convertIPv6Addr(String ipAddr) {
        if (StringUtils.isBlank(ipAddr)) {
            return "";
        }
        if (!isValidPartOfIPv6(ipAddr.trim())) {
            return "";
        }
        ipAddr = ipAddr.trim();
        StringBuilder sb = new StringBuilder();

        if (ipAddr.indexOf("::") != -1) {
            String[] ips = StringUtils.splitByWholeSeparator(ipAddr, "::");

            if ((ips.length == 2) && (StringUtils.isNotBlank(ips[0])) && (StringUtils.isNotBlank(ips[1]))) {
                String[] left = StringUtils.split(ips[0].trim(), ":");
                String[] right = StringUtils.split(ips[1].trim(), ":");
                sb.append(repeatStrBy0(left));
                int length = 8 - (left.length + right.length);
                for (int i = 0; i < length; i++) {
                    sb.append(":");
                    sb.append(StringUtils.repeat("0", 4));
                }

                sb.append(":");
                sb.append(repeatStrBy0(right));
            } else if (ipAddr.startsWith("::")) {
                if (ips.length == 0) {
                    for (int i = 0; i < 7; i++) {
                        sb.append(StringUtils.repeat("0", 4));
                        sb.append(":");
                    }
                    sb.append(StringUtils.repeat("0", 4));
                } else {
                    String[] right = StringUtils.split(ips[0].trim(), ":");
                    int length = 8 - right.length;
                    for (int i = 0; i < length; i++) {
                        if (i == 7) {
                            sb.append(StringUtils.repeat("0", 4));
                        } else {
                            sb.append(StringUtils.repeat("0", 4));
                            sb.append(":");
                        }
                    }
                    sb.append(repeatStrBy0(right));
                }
            } else {
                String[] left = StringUtils.split(ips[0].trim(), ":");
                sb.append(repeatStrBy0(left));
                int length = 8 - left.length;
                for (int i = 0; i < length; i++) {
                    sb.append(":");
                    sb.append(StringUtils.repeat("0", 4));
                }
            }
        } else {
            String[] str = StringUtils.split(ipAddr, ":");

            if ((ipAddr.startsWith(":")) && (ipAddr.endsWith(":")) && (StringUtils.countMatches(ipAddr, ":") == 2)) {
                sb.append(":");
                sb.append(repeatStrBy0(str));
                sb.append(":");
            } else if (ipAddr.startsWith(":")) {
                sb.append(":");
                sb.append(repeatStrBy0(str));
            } else if (ipAddr.endsWith(":")) {
                sb.append(repeatStrBy0(str));
                sb.append(":");
            } else {
                sb.append(repeatStrBy0(str));
            }
        }
        return sb.toString().toUpperCase();
    }

    private static String repeatStrBy0(String[] strs) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strs.length; i++) {
            if (i == 0) {
                sb.append(StringUtils.repeat("0", 4 - strs[i].trim().length()));
                sb.append(strs[i].trim());
            } else {
                sb.append(":");
                sb.append(StringUtils.repeat("0", 4 - strs[i].trim().length()));
                sb.append(strs[i].trim());
            }
        }
        return sb.toString();
    }

    private static BigInteger convertHostIpv6ToBigInt(String hostIp) {
        if (null == hostIp) {
            return BigInteger.ZERO;
        }

        String newIpv6 = convertIPv6Addr(hostIp);
        StringTokenizer st = new StringTokenizer(newIpv6, ":");
        if (st.countTokens() != 8) {
            return BigInteger.ZERO;
        }

        try {
            Inet6Address inetAddr = (Inet6Address) Inet6Address.getByName(hostIp);

            byte[] addr = inetAddr.getAddress();
            return new BigInteger(addr);
        } catch (UnknownHostException e) {
            log.warn(null, e);
        }
        return BigInteger.ZERO;
    }

    public static boolean isLegalIpV6Common(String ip) {
        try {
            InetAddress addr = Inet6Address.getByName(ip);
            if ((addr instanceof Inet6Address))
                ((Inet6Address) addr).getHostAddress();
            else {
                return false;
            }
            return true;
        } catch (Exception e) {
            log.warn(null, e);
        }
        return false;
    }

    public static boolean isValidPartOfIPv6(String ipAddr) {
        if (ipAddr.indexOf("::") != -1) {
            if (isLegalIpV6Common(ipAddr))
                return true;
        } else {
            String regHex = "(\\p{XDigit}{1,4})";
            String regIPv61 = "^(" + regHex + ":){0,7}" + regHex + "$";
            String regIPv62 = "^:(" + regHex + ":){0,6}" + regHex + "$";
            String regIPv63 = "^(" + regHex + ":){0,6}" + regHex + ":$";
            String regIPv64 = "^:(" + regHex + ":){0,6}";
            String regIPv6 = "^(" + regIPv61 + ")|(" + regIPv62 + ")|(" + regIPv63 + ")|(" + regIPv64 + ")$";

            if (ipAddr.matches(regIPv6)) {
                return true;
            }
        }
        return false;
    }

    public static String convertLongToHostIpv4(BigInteger ip) {
        long longIp = ip == null ? 0L : ip.longValue();
        return String.valueOf(longIp >> 24 & 0xFF) + "." + (longIp >> 16 & 0xFF) + "." + (longIp >> 8 & 0xFF) + "." + (
            longIp & 0xFF);
    }

    public static String convertBigIntToHostIpv6(BigInteger ip) {
        if ((ip == null) || (ip.longValue() == 0L)) {
            return "";
        }
        if (ip.bitLength() <= 32) {
            return convertLongToHostIpv4(ip);
        }
        String result = "";
        StringBuilder buffer = new StringBuilder();
        byte[] addr = convertBigIntTo16Bytes(ip);
        int count = 0;
        for (byte by : addr) {
            count++;
            String value = "00" + Integer.toHexString(convertNegative(by)).toUpperCase();

            value = value.substring(value.length() - 2);
            buffer.append(value);
            if (count % 2 == 0) {
                buffer.append(":");
            }
        }
        buffer.delete(buffer.length() - 1, buffer.length());
        result = buffer.toString();

        return result;
    }

    public static byte[] convertBigIntTo16Bytes(BigInteger ip) {
        byte[] temp = new byte[16];

        Arrays.fill(temp, (byte) 0);
        byte[] ipv6Array = ip.toByteArray();
        System.arraycopy(ipv6Array, 0, temp, 16 - ipv6Array.length, ipv6Array.length);

        return temp;
    }

    public static String convertBigIntToHostIp(BigInteger ip) {
        if (ip == null) {
            return "";
        }
        if (ip.bitLength() <= 32) {
            return convertLongToHostIpv4(ip);
        }
        return convertBigIntToHostIpv6(ip);
    }

    public static int convertNegative(byte by) {
        int result = by;
        if (result < 0) {
            result = result << 24 >>> 24;
        }
        return result;
    }

    public static String convertLongToHexString(long value) {
        String result = Long.toHexString(value);
        int length = result.length();

        for (int i = 0; i < 16 - length; i++) {
            result = "0" + result;
        }
        return result;
    }

    public static boolean isValidIpv4(String ipStr) {
        if (null == ipStr) {
            return false;
        }

        StringTokenizer st = new StringTokenizer(ipStr, ".");
        if (st.countTokens() != 4) {
            return false;
        }
        try {
            while (st.hasMoreTokens()) {
                int ip = Integer.parseInt(st.nextToken());

                if ((ip < 0) || (ip > 255))
                    return false;
            }
        } catch (Exception e) {
            log.warn(null, e);
            return false;
        }
        return true;
    }

    public static String hex2bin(String hex) throws UnsupportedEncodingException {
        String hexToUpper = hex.toUpperCase();
        String digital = "0123456789ABCDEF";
        char[] hex2char = hexToUpper.toCharArray();
        byte[] bytes = new byte[hexToUpper.length() / 2];

        for (int i = 0; i < bytes.length; i++) {
            int temp = digital.indexOf(hex2char[(2 * i)]) * 16;
            temp += digital.indexOf(hex2char[(2 * i + 1)]);
            bytes[i] = (byte) (temp & 0xFF);
        }
        return new String(bytes, "UTF-8");
    }

    public static String getStringMD5(String input) throws UnsupportedEncodingException {
        if (input == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");

            byte[] inputByteArray = input.toUpperCase().getBytes("UTF-8");

            messageDigest.update(inputByteArray);

            byte[] resultByteArray = messageDigest.digest();

            return byteArrayToHex(resultByteArray);
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }

    private static String byteArrayToHex(byte[] byteArray) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        char[] resultCharArray = new char[byteArray.length * 2];

        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[(index++)] = hexDigits[(b >>> 4 & 0xF)];
            resultCharArray[(index++)] = hexDigits[(b & 0xF)];
        }

        return new String(resultCharArray);
    }

    public static String fillIPv6Str(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        int index = value.contains("%") ? value.indexOf("%") : value.indexOf("/");
        if (index != -1) {
            value = value.substring(0, index);
        }

        String ndcValue = replaceIPv6DoubleColon(value);
        if (checkIPv6(ndcValue)) {
            String[] segmentValue = ndcValue.split(":");
            return repeatStrBy0(segmentValue).toUpperCase();
        }

        return value;
    }

    private static boolean checkIPv6(String value) {
        if (StringUtils.isBlank(value)) {
            return false;
        }

        value = replaceIPv6DoubleColon(value);
        if (value == null) {
            return false;
        }

        boolean result = false;
        String regHex = "([0-9a-fA-F]{1,4})";
        String regIPv6Full = "^(" + regHex + ":){7}" + regHex + "$";
        Pattern pattern = Pattern.compile(regIPv6Full);
        result = pattern.matcher(value).matches();

        return result;
    }

    private static String replaceIPv6DoubleColon(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        str = str.trim();
        if (str.equals("::")) {
            return "0:0:0:0:0:0:0:0";
        }

        int count = countTimesContains(str, "::");
        if (count > 1)
            return null;
        if (count == 1) {
            if (str.startsWith("::")) {
                String tempStr = str.substring("::".length());
                int tempCount = countTimesContains(tempStr, ":");
                if (tempCount > 6) {
                    return null;
                }
                int i = 7 - tempCount;
                int j = 0;
                StringBuilder stringBuilder = new StringBuilder();
                while (j < i) {
                    stringBuilder.append("0:");
                    j++;
                }
                return stringBuilder.append(tempStr).toString();
            }
            if (str.endsWith("::")) {
                String tempStr = str.substring(0, str.length() - "::".length());
                int tempCount = countTimesContains(tempStr, ":");
                if (tempCount > 6) {
                    return null;
                }
                int i = 7 - tempCount;
                int j = 0;
                StringBuilder stringBuilder = new StringBuilder(tempStr);
                while (j < i) {
                    stringBuilder.append(":0");
                    j++;
                }
                return stringBuilder.toString();
            }

            int index = str.indexOf("::");
            String tempBeforeStr = str.substring(0, index);
            String tempAfterStr = str.substring(index + "::".length());
            int tempCount = countTimesContains(tempBeforeStr, ":") + countTimesContains(tempAfterStr, ":");

            if (tempCount > 5) {
                return null;
            }
            int i = 7 - tempCount;
            int j = 0;
            StringBuilder stringBuilder = new StringBuilder(tempBeforeStr);

            while (j < i - 1) {
                stringBuilder.append(":0");
                j++;
            }
            return stringBuilder.append(":").append(tempAfterStr).toString();
        }

        return str;
    }

    private static int countTimesContains(String str, String sub) {
        if ((StringUtils.isBlank(str)) || (StringUtils.isBlank(sub))) {
            return 0;
        }
        int count = 0;
        int index = -sub.length();
        do {
            index = str.indexOf(sub, index + sub.length());
            if (index >= 0)
                count += 1;
        }
        while ((index >= 0) && (index + sub.length() <= str.length()));
        return count;
    }

    // IP字符串转成Long型整数
    public static Long convertIpStringToLong(String ip) {
        String[] ips = ip.split("\\.");
        return (Long.parseLong(ips[0]) << 24) | (Long.parseLong(ips[1]) << 16)
                | (Long.parseLong(ips[2]) << 8) | (Long.parseLong(ips[3]));
    }

}
