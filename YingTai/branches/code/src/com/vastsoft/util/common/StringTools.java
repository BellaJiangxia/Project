package com.vastsoft.util.common;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 一系列的字符串工具<br>
 * 字符串工具，专门用来处理字符串
 *
 * @author jben
 * @since v0.0001
 */
public class StringTools {
    /**
     * 格式化字符串，隐藏开始的指定位字符，使用指定字符替换,在格式化之前会对字符串去空
     *
     * @param src
     * @param len
     * @param flag
     * @return
     */
    public static String formatBeginStringWithTrim(String src, int len, char flag) {
        return formatBeginString(strTrim(src), len, flag);
    }

    /**
     * 格式化字符串，隐藏开始的指定位字符，使用指定字符替换
     *
     * @param src
     * @param len
     * @param flag
     * @return
     */
    public static String formatBeginString(String src, int len, char flag) {
        if (src == null)
            return null;
        if (len <= 0)
            return src;
        StringBuilder p = new StringBuilder();
        for (int i = 0; i < len; i++)
            p.append(flag);
        if (src.length() <= len)
            return p.toString();
        return p.append(src.substring(len)).toString();
    }

    /**
     * 格式化字符串，隐藏最后的指定位字符，使用指定字符替换,在格式化之前会对字符串去空
     *
     * @param src
     * @param len
     * @param flag
     * @return
     */
    public static String formatEndStringWithTrim(String src, int len, char flag) {
        return formatEndString(strTrim(src), len, flag);
    }

    /**
     * 格式化字符串，隐藏最后的指定位字符，使用指定字符替换
     *
     * @param src
     * @param len
     * @param flag
     * @return
     */
    public static String formatEndString(String src, int len, char flag) {
        if (src == null)
            return null;
        if (len <= 0)
            return src;
        StringBuilder p = new StringBuilder();
        for (int i = 0; i < len; i++)
            p.append(flag);
        if (src.length() <= len)
            return p.toString();
        return src.substring(0, src.length() - len) + p.toString();
    }

    /**
     * 去掉字符串数组中空字符串
     */
    public static List<String> strListTrim(List<String> strs) {
        if (strs == null)
            return null;
        List<String> result = new ArrayList<String>();
        if (strs.size() <= 0)
            return result;
        for (String str : strs) {
            if (str == null)
                continue;
            if (str.trim().isEmpty())
                continue;
            result.add(str);
        }
        return result;
    }

    /**
     * 保证以指定字符结束，及如果不是就加上
     */
    public static String endWith(String src, char en) {
        if (src == null || src.length() <= 0) {
            return String.valueOf(en);
        }
        char end = src.charAt(src.length() - 1);
        if (end == en)
            return src;
        return src + String.valueOf(en);
    }

    /**
     * 保证以指定字符串结束，及如果不是就加上
     */
    public static String endWith(String src, String endStr) {
        if (endStr == null || endStr.length() == 0)
            return src;
        if (src == null || src.length() == 0)
            return endStr;
        if (src.endsWith(endStr))
            return src;
        return src + endStr;
    }

    /**
     * 去掉字符串开始的指定部分，如果不在开始或没有，抛出异常
     */
    public static String deductChars(String src, String chars) {
        if (!src.startsWith(chars))
            throw new RuntimeException("字符串不是这样的开始");
        return src.substring(chars.length());
    }

    /*** 根据Unicode编码完美的判断中文汉字和符号 */
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    /*** 完整的判断是否存在中文汉字和符号 */
    public static boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    /*** 只能判断部分CJK字符（CJK统一汉字） */
    public static boolean isChineseByREG(String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("[\\u4E00-\\u9FBF]+");
        return pattern.matcher(str.trim()).find();
    }

    /*** 只能判断部分CJK字符（CJK统一汉字） */
    public static boolean isChineseByName(String str) {
        if (str == null) {
            return false;
        }
        // 大小写不同：\\p 表示包含，\\P 表示不包含
        // \\p{Cn} 的意思为 Unicode 中未被定义字符的编码，\\P{Cn} 就表示 Unicode中已经被定义字符的编码
        String reg = "\\p{InCJK Unified Ideographs}&&\\P{Cn}";
        Pattern pattern = Pattern.compile(reg);
        return pattern.matcher(str.trim()).find();
    }

    /**
     * 判断字符串是否符合用户名规范
     */
    public static boolean isUserName(String src) {
        if (src == null)
            return false;
        int len = src.length();
        if (len <= 0)
            return false;
        for (int i = 0; i < len; i++) {
            char ch = src.charAt(i);
            if (ch >= 65 && ch <= 90)
                continue;
            if (ch >= 97 && ch <= 122)
                continue;
            if (ch == 95)
                continue;
            if (ch >= 48 && ch <= 57)
                continue;
            return false;
        }
        return true;
    }

    /**
     * 检查字符串是否只含字母数字和英文附号
     */
    public static boolean isUniChar(String src) {
        if (src == null)
            return false;
        int len = src.length();
        if (len <= 0)
            return false;
        for (int i = 0; i < len; i++) {
            char ch = src.charAt(i);
            if (ch > 255)
                return false;
        }
        return true;
    }

    /**
     * 检查字符串是否是手机号
     */
    public static boolean isMobileNum(String mobiles) {
        if (mobiles == null)
            return false;
        mobiles = mobiles.trim();
        if (mobiles.length() != 11)
            return false;
        if (!isNumber(mobiles))
            return false;
        return true;
    }

    /**
     * 将byte数组转换为16进制字符串
     */
    public static String toHexString(byte value[]) {
        StringBuilder newString = new StringBuilder();
        for (byte b : value) {
            String str = Integer.toHexString(b);
            if (str.length() > 2)
                str = str.substring(str.length() - 2);
            if (str.length() < 2)
                str = "0" + str;
            newString.append(str);
        }
        return newString.toString();
    }

    /**
     * 将16进制字符串转换为字节数组
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals(""))
            return null;
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            String sub = hexString.substring(pos, pos + 2);
            d[i] = (byte) Integer.parseInt(sub, 16);
        }
        return d;
    }

    /**
     * 判断字符串的最大长度是否是指定长度
     */
    public static boolean isValidString(String str, int length) {
        boolean flag = true;
        if (str != null && !str.isEmpty() && length > 0) {
            if (str.length() > length)
                flag = false;
        }
        return flag;
    }

    /**
     * 判断是否是密码，不包含空格和中文等
     */
    public static boolean isPassword(String password, int minLen, int maxLen) {
        if (password == null)
            return false;
        if (password.trim().isEmpty())
            return false;
        if (minLen != 0)
            if (password.length() < minLen)
                return false;
        if (maxLen != 0)
            if (password.length() > maxLen)
                return false;
        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (ch > 122)
                return false;
            if (ch < 33)
                return false;
        }
        return true;
    }

    /**
     * 检查是否是IP地址
     */
    public static boolean isIpAddr(String addr) {
        if (addr == null || addr.trim().isEmpty())
            return false;
        addr = addr.trim();
        try {
            String[] strs = splitString(addr, '.');
            if (strs == null || strs.length != 4)
                return false;
            for (String string : strs) {
                int appp = Integer.valueOf(string);
                if (appp < 0 || appp > 255)
                    return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 去掉字符串数组中空字符串
     */
    public static List<String> strArrayTrim(String[] strs) {
        if (strs == null)
            return null;
        List<String> result = new ArrayList<String>();
        if (strs.length <= 0)
            return result;
        for (String str : strs) {
            if (str == null)
                continue;
            if (str.trim().isEmpty())
                continue;
            result.add(str);
        }
        return result;
    }

    /**
     * 去掉字符串数组中空字符串
     */
    public static List<String> strArrayTrim(List<String> strs) {
        if (strs == null)
            return null;
        List<String> result = new ArrayList<String>();
        if (strs.size() <= 0)
            return result;
        for (String str : strs) {
            if (str == null)
                continue;
            if (str.trim().isEmpty())
                continue;
            result.add(str);
        }
        return result;
    }

    /**
     * 字符串转换unicode
     */
    public static String string2Unicode(String string) {
        if (isEmpty(string))
            return "";
        StringBuilder unicode = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            // 取出每一个字符
            char c = string.charAt(i);
            // 转换为unicode
            unicode.append("\\u" + Integer.toHexString(c));
        }
        return unicode.toString();
    }

    /**
     * unicode 转字符串
     */
    public static String unicode2String(String unicode) {
        if (isEmpty(unicode))
            return "";
        StringBuilder string = new StringBuilder();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++) {
            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);
            // 追加成string
            string.append((char) data);
        }
        return string.toString();
    }

    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    /**
     * 将字符串数组已分隔符的间隔输出为字符串
     *
     * @param <T>
     */
    public static <T> String arrayToString(T[] src, char splitChar) {
        if (src == null || src.length <= 0)
            return null;
        StringBuilder sbb = new StringBuilder();
        for (int i = 0; i < src.length; i++) {
            if (i > 0)
                sbb.append(splitChar);
            T sss = src[i];
            if (sss == null || sss.toString() == null)
                continue;
            String str = sss.toString().trim();
            if (str.isEmpty())
                continue;
            sbb.append(str);
        }
        return sbb.toString();
    }

    public static long[] strTolongArray(String str, char c) {
        String[] res = splitString(str, c);
        if (res == null)
            return null;
        long[] result = new long[res.length];
        for (int i = 0; i < res.length; i++) {
            result[i] = Long.parseLong(res[i]);
        }
        return result;
    }

    /**
     * 格式化字符串，将字符串中间的字符替换为指定字符
     *
     * @param str      要格式化的字符串
     * @param beginLen 开始的字符串长度
     * @param endLen   结束的字符串长度
     * @param flag     填充字符
     * @return
     */
    public static String formatBetweenStr(String str, int beginLen, int endLen, char flag) {
        if (str == null)
            return "";
        str = str.trim();
        if (str.length() <= beginLen + endLen)
            return str;
        StringBuilder sbb = new StringBuilder();
        for (int i = 0; i < beginLen; i++) {
            sbb.append(str.charAt(i));
        }
        for (int i = 0; i < str.length() - beginLen - endLen; i++) {
            sbb.append(flag);
        }
        for (int i = str.length() - endLen; i < str.length(); i++) {
            sbb.append(str.charAt(i));
        }
        return sbb.toString();
    }

    /**
     * 格式化字符串src，返回 len长度的字符串，将在ֵsrc前面加指定填充符
     *
     * @param src  源字符串
     * @param len  字符串长度
     * @param flag 填充字符
     * @return 返回得到的字符串
     */
    public static String formatString(String src, int len, char flag) {
        if (src == null)
            src = "";
        if (src.length() >= len)
            return src;
        int ll = len - src.length();
        StringBuilder Strin = new StringBuilder();
        for (int i = 0; i < ll; i++)
            Strin.append(flag);
        Strin.append(src);
        return Strin.toString();
    }

    public static String formatIdentityId(String str) {
        return formatBetweenStr(str, 5, 4, '*');
    }

    /**
     * 将一个字符串转换成行字符串，返回行数组，返回值中不包含空行
     */
    public static String[] stringToLines(String content) {
        if (content == null)
            return null;
        content = content.trim();
        if (content.isEmpty())
            return null;
        content = content.replaceAll("\r\n", "\n");
        return content.split("\n");
    }

    public static boolean contains(String srcStr, String sq) {
        if (srcStr == null || sq == null)
            return false;
        return srcStr.contains(sq);
    }

    /**
     * 对字符串去掉指定字符
     */
    public static String removeChars(String src, char... ch) {
        if (ch == null || ch.length <= 0)
            return src;
        if (src == null)
            return src;
        if (src.isEmpty())
            return src;
        Arrays.sort(ch);
        int len = src.length();
        StringBuilder result = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = src.charAt(i);
            if (Arrays.binarySearch(ch, c) >= 0)
                continue;
            result.append(c);
        }
        return result.toString();
    }

    /**
     * 去掉除中文和英文之外的字符
     */
    public static String cutFHchar(String src) {
        if (src == null || src.length() <= 0)
            return src;
        int len = src.length();
        StringBuilder sbb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char ch = src.charAt(i);
            if (ch < 65)
                continue;
            if (ch > 90 && ch < 97)
                continue;
            if (ch > 122 && ch < 128)
                continue;
            sbb.append(ch);
        }
        return sbb.toString();
    }

    /**
     * 对整个字符串去空，去掉不显示的字符
     */
    public static String strTrimGreat(String src) {
        if (src == null || src.length() <= 0)
            return src;
        int len = src.length();
        StringBuilder sbb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char ch = src.charAt(i);
            if (ch <= ' ')
                continue;
            if (ch == '　')
                continue;
            sbb.append(ch);
        }
        return sbb.toString();
    }

    /**
     * 判断两个字符串是否相等，会调用trim()函数<br>
     * 并且只有当两个字符串都不为null或都不为空串才可能返回true
     */
    public static boolean strEqualsWithTrimAndNotEmpty(String str1, String str2) {
        if (str1 == null || str1.trim().isEmpty())
            return false;
        if (str2 == null || str2.trim().isEmpty())
            return false;
        return str1.trim().equals(str2.trim());
    }

    /**
     * 判断两个字符串是否相等，会调用trim()函数<br>
     */
    public static boolean strEqualsWithTrim(String str1, String str2) {
        if (str1 == null)
            return str2 == null;
        if (str2 == null)
            return str1 == null;
        return str1.trim().equals(str2.trim());
    }

    /**
     * 将两个字符串连接起来，并且当其中一个为null时会忽略
     */
    public static String concat(String src, String... otherStrs) {
        if (src == null)
            src = "";
        if (otherStrs == null || otherStrs.length <= 0)
            return src;
        StringBuilder result = new StringBuilder(src);
        for (String string : otherStrs) {
            if (string == null || string.isEmpty())
                continue;
            result.append(string);
        }
        return result.toString();
    }

    /**
     * 比较两个字符串是否相等，当都为null是返回true
     *
     * @param str1
     * @param str2
     * @return 返回是否相等
     * @since 0.0001
     */
    public static boolean strEquals(String str1, String str2) {
        if (str1 == null) {
            if (str2 == null)
                return true;
            return false;
        }
        return str1.equals(str2);
    }

    /**
     * 将数组转换为字符串，用制定的分隔符分割<br>
     * 如果数据为null 或者全是空元素则返回null
     *
     * @param arr
     * @param splitChar
     * @return
     * @since 0.0001
     */
    public static String arrayToString(Object arr, char splitChar) {
        if (arr == null)
            return null;
        if (!arr.getClass().isArray())
            return null;
        int length = Array.getLength(arr);
        int eleCount = 0;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            Object obj = Array.get(arr, i);
            if (obj == null)
                continue;
            if (i > 0)
                result.append(splitChar);
            result.append(obj);
            eleCount++;
        }
        if (eleCount <= 0)
            return null;
        return result.toString();
    }

    /**
     * 字符串去空
     */
    public static String strTrim(String src) {
        if (src == null)
            return null;
        return src.trim();
    }

    /**
     * 判断字符串是否在最大长度之内，且不能为null
     */
    public static boolean checkStr(String src, int maxLen) {
        return checkStr(src, maxLen, false);
    }

    /**
     * 判断字符串时候在最大长度之内，且指定是否可等于null
     */
    public static boolean checkStr(String src, int maxLen, boolean ignoreNull) {
        return checkStr(src, 0, maxLen, ignoreNull);
    }

    /**
     * 判断字符串是否在指定长度范围内，且不可为null
     */
    public static boolean checkStr(String src, int minLen, int maxLen) {
        return checkStr(src, minLen, maxLen, false);
    }

    /**
     * 检查字符串是否合乎长度规则,包前不包后
     */
    public static boolean checkStr(String src, int minLen, int maxLen, boolean ignoreNull) {
        if (src == null || src.trim().isEmpty()) {
            if (ignoreNull)
                return true;
            else
                return false;
        }
        int length = src.length();
        return length >= minLen && length < maxLen;
    }

    /**
     * 将字符串列表已分隔符的间隔输出为字符串
     *
     * @param <T>
     */
    public static <T> String listToString(List<T> src, char splitChar) {
        if (CollectionTools.isEmpty(src))
            return null;
        StringBuilder sbb = new StringBuilder();
        sbb.append(src.get(0));
        if (src.size() < 2)
            return sbb.toString();
        for (int i = 1; i < src.size(); i++) {
            T t = src.get(i);
            if (t == null || t.toString() == null)
                continue;
            sbb.append(splitChar).append(t.toString().trim());
        }
        return sbb.toString();
    }

    /**
     * 将字符串数组已分隔符的间隔输出为字符串
     */
    public static String arrayToString(String[] src, char splitChar) {
        if (src == null || src.length <= 0)
            return null;
        StringBuilder sbb = new StringBuilder();
        sbb.append(src[0]);
        for (int i = 1; i < src.length; i++) {
            String sss = src[i];
            if (sss == null || sss.trim().isEmpty())
                continue;
            sbb.append(splitChar).append(sss.trim());
        }
        return sbb.toString();
    }

    /**
     * 判断字符串是否符合身份证规范
     */
    public static boolean wasIdentityId(String str) {
        if (str == null || str.trim().isEmpty())
            return false;
        str = str.trim();
        if (str.length() == 15) {
            return isNumber(str);
        } else if (str.length() == 18) {
            if (isNumber(str))
                return true;
            String tmpStr = str.substring(0, str.length() - 1);
            if (!isNumber(tmpStr))
                return false;
            char ch = str.charAt(str.length() - 1);
            return Character.toLowerCase(ch) == 'x';
        } else {
            return false;
        }
    }

    /**
     * 判断字符串是否是数字，可判断任意长度
     */
    @Deprecated
    public static boolean isNumber(String str) {
        if (str == null || str.trim().isEmpty())
            return false;
        str = str.trim();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (ch < 48 || ch > 57)
                return false;
        }
        return true;
    }

    /**
     * 判断字符串是否是正整数
     */
    public static boolean isPositiveInteger(String orginal) {
        return isMatch("^\\+{0,1}[1-9]\\d*", orginal);
    }

    /**
     * 判断字符串是否是负整数
     *
     * @param orginal
     * @return
     */
    public static boolean isNegativeInteger(String orginal) {
        return isMatch("^-[1-9]\\d*", orginal);
    }

    /**
     * 判断字符串是否是整数
     *
     * @param orginal
     * @return
     */
    public static boolean isInteger(String orginal) {
        return isMatch("[+-]{0,1}0", orginal) || isPositiveInteger(orginal) || isNegativeInteger(orginal);
    }

    /**
     * 判断字符串是否是正小数
     *
     * @param orginal
     * @return
     */
    public static boolean isPositiveDecimal(String orginal) {
        return isMatch("\\+{0,1}[0]\\.[1-9]*|\\+{0,1}[1-9]\\d*\\.\\d*", orginal);
    }

    /**
     * 判断字符串是否为负小数
     *
     * @param orginal
     * @return
     */
    public static boolean isNegativeDecimal(String orginal) {
        return isMatch("^-[0]\\.[1-9]*|^-[1-9]\\d*\\.\\d*", orginal);
    }

    /**
     * 判断字符串是否是小数
     *
     * @param orginal
     * @return
     */
    public static boolean isDecimal(String orginal) {
        return isMatch("[-+]{0,1}\\d+\\.\\d*|[-+]{0,1}\\d*\\.\\d+", orginal);
    }

    /**
     * 判断字符串是否表示的正数
     *
     * @param orginal
     * @return
     */
    public static boolean isPositive(String orginal) {
        return isPositiveInteger(orginal) || isPositiveDecimal(orginal);
    }

    /**
     * 判断字符串是否表示负数
     *
     * @param orginal
     * @return
     */
    public static boolean isNegative(String orginal) {
        return isNegativeDecimal(orginal) || isNegativeInteger(orginal);
    }

    /**
     * 判断字符串是否是小数或者整数
     *
     * @param orginal
     * @return
     */
    public static boolean isRealNumber(String orginal) {
        return isInteger(orginal) || isDecimal(orginal);
    }

    private static boolean isMatch(String regex, String orginal) {
        if (orginal == null || orginal.trim().equals("")) {
            return false;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher isNum = pattern.matcher(orginal);
        return isNum.matches();
    }

    /**
     * 截断字符串
     */
    public static String cutStr(String src, int newLen) {
        if (src == null)
            return "";
        if (src.length() <= newLen)
            return src;
        return src.substring(0, newLen);
    }

    /**
     * 拆分字符串，拆分符可以是多个
     *
     * @param src    要拆分的字符串
     * @param spCode 拆分符
     * @return 拆分得到的字符数组
     */
    public static List<String> splitStrAsList(String src, char... spCode) {
        if (src == null)
            return null;
        src = src.trim();
        if (src.equals(""))
            return null;
        List<String> strss = new ArrayList<String>();
        Arrays.sort(spCode);
        if (src.length() <= 1) {
            strss.add(src);
        } else {
            int lastindex = 0;
            for (int i = 0; i < src.length(); i++) {
                char c = src.charAt(i);
                if (ArrayTools.contains(spCode, c)) {
                    String tmp = src.substring(lastindex, i);
                    if (tmp != null && !tmp.equals(""))
                        strss.add(tmp);
                    lastindex = i + 1;
                }
                if (i == src.length() - 1) {
                    String tmp = src.substring(lastindex, i + 1);
                    if (tmp != null && !tmp.equals(""))
                        strss.add(tmp);
                    lastindex = i + 1;
                }
            }
        }
        return strss;
    }

    /**
     * 拆分字符串，拆分符可以是多个,拆分结果不包含null或空串
     *
     * @param src    要拆分的字符串
     * @param spCode 拆分符
     * @return 拆分得到的字符数组
     */
    public static String[] splitString(String src, char... spCode) {
        List<String> strss = splitStrAsList(src, spCode);
        return (CollectionTools.isEmpty(strss) ? null : ArrayTools.collectionToArray(strss, String.class));
    }

    public static long[] StrToLongArray(String about_case_ids, char c) {
        String[] res = splitString(about_case_ids, c);
        if (res == null)
            return null;
        long[] result = new long[res.length];
        for (int i = 0; i < res.length; i++) {
            result[i] = Long.parseLong(res[i]);
        }
        return result;
    }

    public static boolean isEmpty(String str) {
        if (str == null)
            return true;
        if (str.trim().isEmpty())
            return true;
        return false;
    }

    public static boolean isPhoneNum(String phone_num) {
        if (phone_num == null)
            return false;
        if (phone_num.length() == 10) {
            return isNumber(phone_num);
        }
        if (phone_num.length() == 11) {
            if (phone_num.contains("-")) {
                String newSTR = phone_num.replace("-", "");
                int newlen = newSTR.length();
                if (newlen != 10)
                    return false;
                int in = phone_num.indexOf('-');
                if (in != 3 && in != 4)
                    return false;
                return isNumber(newSTR);
            } else {
                return isNumber(phone_num);
            }
        }
        if (phone_num.length() == 12) {
            if (phone_num.contains("-")) {
                String newSTR = phone_num.replace("-", "");
                int newlen = newSTR.length();
                if (newlen != 11)
                    return false;
                int in = phone_num.indexOf('-');
                if (in != 3 && in != 4)
                    return false;
                return isNumber(newSTR);
            } else {
                return isNumber(phone_num);
            }
        }
        if (phone_num.length() == 13) {
            if (phone_num.contains("-")) {
                String newSTR = phone_num.replace("-", "");
                int newlen = newSTR.length();
                if (newlen != 12)
                    return false;
                int in = phone_num.indexOf('-');
                if (in != 3 && in != 4)
                    return false;
                return isNumber(newSTR);
            } else {
                return false;
            }
        }
        return false;
    }

    /**
     * 判断字符是否是数值字符
     */
    public static boolean isNum(char ch) {
        if (ch < 48 || ch > 57)
            return false;
        return true;
    }

    /**
     * 去掉不是数值的字符
     */
    public static String cutNotNumChar(String str) {
        StringBuilder sbb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (!isNum(ch))
                continue;
            sbb.append(ch);
        }
        return sbb.toString();
    }

    /**
     * @param src
     * @param str
     * @param ignoreCase 是否忽略大小写
     * @return
     */
    public static boolean beginWith(String src, String str, boolean ignoreCase) {
        if (src == null)
            return false;
        if (str == null)
            return false;
        src = src.toLowerCase();
        str = str.toLowerCase();
        return src.startsWith(str);
    }
}
