package com.kingname.enterprisebackend.utils;

public class CommonUtils {

    public static String refineSeed(String seed) {
        if (seed == null) return null;
        return seed.replaceAll("[*'!\"‘“@:;^\\\\%~\\[\\]_\\-./]", ""); //자동완성 특수 문자 제거
    }
}
