package com.example.tryapplication.Login;

import android.util.Patterns;

import java.util.regex.Pattern;

/**
 *用Regular Expression 做格式判斷
 */
public class Check {
    /**
     * 規則運算式：日期
     */
    public static final String DAY="^[0-9]{4}/[0-1]{1}[0-9]{1}/[0-3]{1}[0-9]{1}$";
    /**
     * 規則運算式：驗證用戶名
     */
    public static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,17}$";

    /**
     * 規則運算式：驗證密碼
     */
    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$";

    /**
     * 移動手機號碼的規則運算式。
     */
    private static final String REGEX_CHINA_MOBILE = "1(3[4-9]|4[7]|5[012789]|8[278])\\d{8}";

    /**
     * 聯通手機號碼的規則運算式。
     */
    private static final String REGEX_CHINA_UNICOM = "1(3[0-2]|5[56]|8[56])\\d{8}";

    /**
     * 電信手機號碼的規則運算式。
     */
    private static final String REGEX_CHINA_TELECOM = "(?!00|015|013)(0\\d{9,11})|(1(33|53|80|89)\\d{8})";

    /**
     * 規則運算式：驗證手機號
     */
    private static final String REGEX_PHONE_NUMBER = "^[0]{1}[9]{1}[0-9]{8}$";


    /**
     * 規則運算式：驗證漢字
     */
    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5],{0,}$";

    /**
     * 規則運算式：驗證身份證
     */
    public static final String REGEX_ID_CARD = "(^\\d{18}$)|(^\\d{15}$)";

    /**
     * 規則運算式：驗證URL
     */
    public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

    /**
     * 規則運算式：驗證IP地址
     */
    public static final String REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";

    /**
     * 校驗用戶名
     *
     * @param username
     * @return 校驗通過返回true，否則返回false
     */
    public static boolean isUsername(String username) {
        return Pattern.matches(REGEX_USERNAME, username);
    }

    /**
     * 校驗密碼
     *
     * @param password
     * @return 校驗通過返回true，否則返回false
     */
    public static boolean isPassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }

    /**
     * 校驗手機號
     *
     * @param mobile
     * @return 校驗通過返回true，否則返回false
     */
    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_PHONE_NUMBER, mobile);
    }


    /**
     * 校驗漢字
     *
     * @param chinese
     * @return 校驗通過返回true，否則返回false
     */
    public static boolean isChinese(String chinese) {
        return Pattern.matches(REGEX_CHINESE, chinese);
    }

    /**
     * 校驗身份證
     *
     * @param idCard
     * @return 校驗通過返回true，否則返回false
     */
    public static boolean isIDCard(String idCard) {
        return Pattern.matches(REGEX_ID_CARD, idCard);
    }

    /**
     * 校驗URL
     *
     * @param url
     * @return 校驗通過返回true，否則返回false
     */
    public static boolean isUrl(String url) {
        return Pattern.matches(REGEX_URL, url);
    }

    /**
     * 校驗IP地址
     *
     * @param ipAddr
     * @return
     */
    public static boolean isIPAddr(String ipAddr) {
        return Pattern.matches(REGEX_IP_ADDR, ipAddr);
    }
    /**
     * 校驗星期
     *
     * @param ipAddr
     * @return
     */
    public static boolean isDAY(String ipAddr) {
        return Pattern.matches(DAY, ipAddr);
    }

    /**
     * 校驗郵箱
     *
     * @param
     * @return 校驗通過返回true，否則返回false
     */
    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}

