package com.kingname.enterprisebackend.utils;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Component
public class DateUtils {

    public static String oneMonthBefore(String date, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        YearMonth newDate = YearMonth.parse(date, formatter);
        YearMonth oneMonthBefore = newDate.minusMonths(1);
        return oneMonthBefore.format(formatter);
    }
}
