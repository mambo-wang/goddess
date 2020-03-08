package com.h3c.vdi.athena.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.h3c.vdi.athena.common.constant.BasicConstant;
import com.h3c.vdi.athena.common.model.DefaultUserDetails;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * VDI常用工具类
 */
public final class Utils {

    private static final Logger log = LoggerFactory.getLogger(Utils.class);

    /**
     * 时间简化成小时：分
     */
    private static final DateTimeFormatter FORMATTER_HHMM = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * 完整时间格式yyyy-MM-dd HH:mm:ss
     */
    private static final DateTimeFormatter FORMATTER_FULL = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 逗号分隔符
     */
    private static final char SEP_COMMA = ',';

    /**
     * 标识服务器系统是否为中文系统
     */
    private static final boolean isZh;

    /**
     * 默认gson转换器，后面根据需要可增加配置
     */
    private static final Gson gson = new GsonBuilder().create();

    /**
     * gson转换器，用于苏亚星字段为null的情况
     */
    private static final Gson gson_suyaxing = new GsonBuilder().serializeNulls().create();

    static {
//        isZh = "zh".equals(Locale.getDefault().getLanguage());
        isZh = true;//当前版本强制使用中文，等支持英文的时候再放开
    }

    private Utils() {
    }

    public static boolean isZh() {
        return isZh;
    }


    /**
     * 将字符转成byte[]
     *
     * @param srcStr 原始字符
     * @return 默认编码的byte[]
     */
    public static byte[] toBytes(Object srcStr) {
        if (srcStr == null) {
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        }
        return String.valueOf(srcStr).getBytes(BasicConstant.CHARSET_UTF8);
    }

    /**
     * 将一个Java对象转成JSON字符串，然后转成byte数组，用于与客户端、Agent等组件的ASN交互
     *
     * @param src 原始Java对象
     * @return Java对象转成JSON字符串后的byte数组
     */
    public static byte[] toAsnCommonRespJson(Object src) {
        if (src == null) {
            return ArrayUtils.EMPTY_BYTE_ARRAY;
        }
        return toBytes(toJson(src));
    }

    /**
     * 格式化日期时间
     *
     * @param time      long类型日期
     * @param formatter 时间格式
     * @return 格式化后日期时间
     */
    public static String formatDate(Long time, DateTimeFormatter formatter) {
        Instant instant = Instant.ofEpochMilli(time);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, BasicConstant.BEIJING_ZONE);
        return localDateTime.format(formatter);
    }

    /**
     * 将日期时间格式化成HH:MM格式
     *
     * @param time long类型日期
     * @return 格式化后日期时间
     */
    public static String formatTimeHHMM(Long time) {
        return formatDate(time, FORMATTER_HHMM);
    }

    /**
     * 增加虚拟机或者存储卷时，组装生成名称
     *
     * @return "IMG_" + "0-9/a-z"并转换为大写的格式
     * 返回名称 共12位
     */
    public static String generateDomainName() {

        Long now = System.currentTimeMillis();
        String suffix = Long.toString(now, 36).toUpperCase();

        return "IMG_" + suffix;
    }


    /**
     * 将日期时间格式化成yyyy-MM-dd HH:mm:ss格式
     *
     * @param datetime long类型日期时间
     * @return 格式化后日期时间
     */
    public static String formatFullDateTime(Long datetime) {
        return formatDate(datetime, FORMATTER_FULL);
    }

    /**
     * 将日期时间格式化成yyyy-MM-dd格式
     *
     * @param datetime long类型日期时间
     * @return 格式化后日期时间
     */
    public static String formatDate(Long datetime) {
        return formatDate(datetime, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * 将string类型的时间转化为Date类型
     * @param strTime 时间
     * @param strFormat 时间格式，可以为："yyyy-MM-dd HH:mm:ss"或者"yyyy-MM-dd"
     * @return
     */
    public static Date stringToDate(String strTime, String strFormat)
    {
        Date date=new Date();
        SimpleDateFormat formatter=new SimpleDateFormat(strFormat);
        try
        {
            date = formatter.parse(strTime);
        }catch (ParseException e)
        {
            log.warn("Failed to get date from string,ParseException.", e);
        }
        return date;
    }

    /**
     * 将string类型的时间转化为Long类型
     * @param strTime 时间
     * @param strFormat 时间格式，可以为："yyyy-MM-dd HH:mm:ss"或者"yyyy-MM-dd"
     * @return
     */
    public static Long stringToLong(String strTime, String strFormat)
    {
        Date date=stringToDate(strTime,strFormat);
        return date.getTime();
    }
    /**
     * 获取某日期为周几
     */
    public static Integer getDayOfWeek(Date date)
    {
        Integer day=null;
        Calendar c=Calendar.getInstance();
        c.setTime(date);
        if(c.get(Calendar.DAY_OF_WEEK)==1)
        {
            day=7;
        }else{
            day=c.get(Calendar.DAY_OF_WEEK)-1;
        }
        return day;
    }

    /**
     * @return 当前时间戳
     */
    public static Long getNowEpochMilli() {
        return Instant.now().toEpochMilli();
    }


    /**
     * 数据库模糊查询拼接，按任意匹配方式，前后追加%。
     *
     * @param val 查询条件的值
     * @return 任意匹配值：%val%
     */
    public static String like(String val) {
        return val == null ? "%" : "%" + val + "%";
    }

    /**
     * 将数组对象以逗号拼接成字符串
     *
     * @param objects 目标数组
     * @return 按逗号拼接的字符串
     */
    public static String join(Object... objects) {
        return StringUtils.join(objects, SEP_COMMA);
    }

    /**
     * 将集合中的数据以逗号拼接成字符串
     *
     * @param collection 目标集合
     * @return 按逗号拼接的字符串
     */
    public static String join(Collection collection) {
        String join = StringUtils.join(collection, SEP_COMMA);
        return join == null ? "" : join;
    }



    /**
     * 把一个JSON字符串转成对象
     *
     * @param json     原始json字符串
     * @param classOfT 对象类型
     * @param <T>      T对象类型
     * @return JSON转成的Java对象
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    /**
     * 将一个Java对象转成JSON字符串
     *
     * @param src 原始对象
     * @return Java对象转成的JSON字符串
     */
    public static String toJson(Object src) {
        return gson.toJson(src);
    }

    /**
     * 将一个Java对象转成JSON字符串（字段带null）
     *
     * @param src 原始对象
     * @return Java对象转成的JSON字符串
     */
    public static String toJsonNull(Object src) {
        return gson_suyaxing.toJson(src);
    }

    /**
     * 获取两个MAC区间内的所有MAC地址
     *
     * @param macStart 起始Mac
     * @param macEnd   结束Mac
     * @return 起始Mac和结束Mac之间所有Mac的集合
     * @throws NullPointerException if @macStart is null or @macEnd is null
     */
    public static List<String> getBetweenMac(String macStart, String macEnd) {
        Objects.requireNonNull(macStart);
        Objects.requireNonNull(macEnd);

        List<String> macList = new LinkedList<>();
        long start = turnMacToLong(macStart);
        long end = turnMacToLong(macEnd);
        String prefix = macStart.substring(0, 9);
        String hex = null;
        String suffix = null;
        StringBuffer mac = null;
        for (long i = start; i < end + 1; i++) {
            hex = Long.toHexString(i);
            suffix = hex.substring(hex.length() - 6);
            mac = new StringBuffer(suffix);
            mac.insert(2, ":");
            mac.insert(5, ":");
            macList.add(prefix + mac.toString());
            System.out.println(prefix + mac.toString());
        }
        return macList;
    }

    /**
     * 将MAC转换成数字
     */
    private static long turnMacToLong(String mac) {
        String hex = mac.replaceAll(":", "");
        long longMac = Long.parseLong(hex, 16);
        return longMac;
    }

    /**
     * 解密
     * @param cryptoText
     * @return  string
     */

    public static String decryptSimple(String cryptoText) {
        byte[] decodedText = Base64.decodeBase64(cryptoText.getBytes(BasicConstant.CHARSET_UTF8));
        if (decodedText == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < decodedText.length; i++) {
            builder.append((char) decodedText[i]);
        }
        return builder.toString();
    }


    public static String orElseBlank(Object input) {
        if (Objects.isNull(input)) {
            return "";
        }
        return orElse(input.toString(), "");
    }

    public static <T>T orElse(T input, T orElse) {
        return Objects.isNull(input) ? orElse : input;
    }

    public static DefaultUserDetails getLoginUser(){
        DefaultUserDetails user = (DefaultUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setPassword(null);
        return user;
    }

    public static String getLoginUsername(){
        DefaultUserDetails user = (DefaultUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getUsername();
    }

    /**
     * 中文转换为unicode编码
     * @param gbString
     * @return
     */
    public static String gbEncoding(final String gbString) {
        char[] utfBytes = gbString.toCharArray();
        String unicodeBytes = "";
        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex ++){
            String hexB = Integer.toHexString(utfBytes[byteIndex]);
            if (hexB.length() <= 2){
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        return unicodeBytes;
    }
}
