package com.kingname.enterprisebackend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data @ToString
@NoArgsConstructor @AllArgsConstructor
public class IndustryStatistic {
    private String id;
    private String date;
    private int year;
    private int month;
    private String companyIndustryCode;
    private String companyIndustryName;
    private int industryCompanyCount;
    private int totalMemberCount;
    private int newMemberCount;
    private int lostMemberCount;
    private long industryAverageSalary;
    private long industryUpperQuartileSalary;
    private long industryLowerQuartileSalary;
    private long industryMedianSalary;
    private String regDt;

}
