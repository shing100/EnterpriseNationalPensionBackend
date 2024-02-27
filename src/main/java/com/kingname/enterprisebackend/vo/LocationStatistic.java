package com.kingname.enterprisebackend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @NoArgsConstructor @AllArgsConstructor @ToString
public class LocationStatistic {
    private String id;
    private String date;
    private int year;
    private int month;
    private String locationName;
    private Long locationCompanyCount;
    private int totalMemberCount;
    private int newMemberCount;
    private int lostMemberCount;
    private long locationAverageSalary;
    private long locationUpperQuartileSalary;
    private long locationLowerQuartileSalary;
    private long locationMedianSalary;
    private String regDt;
}
