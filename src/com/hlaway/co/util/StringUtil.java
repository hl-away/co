package com.hlaway.co.util;

/**
 * User: hl-away
 * Date: 25.10.13
 */
public class StringUtil {
    public static String getLastLetter(String str) {
        String letter = getLetter(str, str.length() - 1);
        if(!validateLastLetter(letter)) {
            letter = getLastLetter(str.substring(0, str.length() - 1));
        }
        return letter;
    }

    public static String getFirstLetter(String str) {
        return getLetter(str, 0);
    }

    public static boolean validateLastLetter(String letter) {
        return !("ь".equalsIgnoreCase(letter) || "ъ".equalsIgnoreCase(letter));
    }

    public static String getLetter(String str, int index) {
        return String.valueOf(str.charAt(index));
    }

    public static String normCityName(String cityName) {
        return getFirstLetter(cityName).toUpperCase() +
                cityName.substring(1, cityName.length());
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty() || str.equalsIgnoreCase(NetworkUtil.NULL);
    }

    public static boolean notEmpty(String str) {
        return !isEmpty(str);
    }

    public static String norm(String str) {
        if(isEmpty(str)) {
            return "";
        }
        return str;
    }

    public static String addParamToURL(String url, String param, Object value) {
        if(!url.contains("?")) {
            url += "?";
        } else {
            url += "&";
        }
        url += param + "=" + String.valueOf(value);
        return url;
    }
}
