package com.h3c.vdi.athena.common.utils;

import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 拼音转换工具类
 */
public class PinyinUtils {

    /**
     * 汉字转换位汉语拼音简拼，英文字符不变，特殊字符丢失 支持多音字
     * @param chines 汉字
     * @return 拼音简拼
     */
    public static String getShortPinyin(String chines) {
        return PinyinHelper.getShortPinyin(filterString(chines));
    }

    /**
     * 汉字转换为汉语拼音全拼，英文字符不变，特殊字符丢失，支持多音字
     * @param chines
     * @return
     */
    public static String getFullPinyin(String chines) {
        return PinyinHelper.convertToPinyinString(filterString(chines), "", PinyinFormat.WITHOUT_TONE);
    }

    /**
     * 过滤掉特殊字符
     * @param str
     * @return
     */
    private static String filterString(String str) {
        String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？ ]";
        Pattern p   =   Pattern.compile(regEx);
        Matcher m   =   p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 中文名称转拼音
     * @param name
     * @param limit
     * @return
     */
    public static String toPinyin(String name, Integer limit) {
        String pinyin = PinyinUtils.getShortPinyin(name);

        //拼音长度与限制长度一致
        if(pinyin.length() >= limit) {
            return pinyin;
        }

        Random r = new Random();

        //拼音长度比限制长度小一位
        if(pinyin.length() == limit -1) {
            return pinyin + r.nextInt(10);
        }

        int bound = (limit - pinyin.length() -1) <= 4 ? (limit - pinyin.length() -1) : 4;
        return pinyin+"_" + r.nextInt((int) Math.pow(10, bound));
    }

}
