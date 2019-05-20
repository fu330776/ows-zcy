package com.goodsogood.ows.helper;

public class DecipherUtils {
    public static String appedZero(String str, int length) {
        return str.length() > length ? str.substring(0, length) : str.length() < length ? String.format("%" + length + "s", str).replace(" ", "0") : str;
    }
}
