package com.kingname.enterprisebackend.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class DateUtils {

    public static String oneMonthBefore(String date, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDate newDate = LocalDate.parse(date, formatter);
        LocalDate oneMonthBefore = newDate.minusMonths(1);
        return oneMonthBefore.format(formatter);
    }
}
