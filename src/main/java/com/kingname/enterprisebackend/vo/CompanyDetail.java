package com.kingname.enterprisebackend.vo;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor
@ToString @Builder
public class CompanyDetail {
    private String id;
    private String date;
    private LocalDate dateDt;
    private int year;
    private int month;
    private String companyName;
    private String originalCompanyName;
    private String csn;
    private int companyStatusCode;
    private String zipCode;
    private String companyJibunAddress;
    private String companyRoadNameAddress;
    private String companyTypeCode;
    private String companyIndustryCode;
    private String companyIndustryName;
    private LocalDate applicationDate;
    private LocalDate reRegistrationDate;
    private LocalDate withdrawalDate;
    private int totalMemberCount;
    private long currentMonthDueAmount;
    private int newMemberCount;
    private int lostMemberCount;
    private long averageSalary;
    private long industryAverageSalary;
    private long industryUpperQuartileSalary;
    private long industryLowerQuartileSalary;
    private long industryMedianSalary;
    private LocalDateTime regDt;
}
