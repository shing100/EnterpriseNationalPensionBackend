package com.kingname.enterprisebackend.utils;

public class CommonUtils {

    public static String refineSeed(String seed) {
        return seed.replaceAll("[*'!\"‘“@:;^\\\\%~\\[\\]_\\-./]", ""); //자동완성 특수 문자 제거
    }
}
