package com.kingname.enterprisebackend.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtils {

    public static BigDecimal calculateRate(BigDecimal current, BigDecimal previous) {
        if (previous.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return current.subtract(previous).divide(previous, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
    }
}
