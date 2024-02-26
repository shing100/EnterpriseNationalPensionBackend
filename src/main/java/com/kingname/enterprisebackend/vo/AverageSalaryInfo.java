package com.kingname.enterprisebackend.vo;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @ToString @Builder
@NoArgsConstructor @AllArgsConstructor
public class AverageSalaryInfo {
    private BigDecimal monthlyAverageSalary;
    private BigDecimal medianSalary;
    private BigDecimal latestMonthAverageSalary;
    private BigDecimal latestMonthMedianSalary;
    private BigDecimal latestMonthAverageSalaryGrowthRate;
    private BigDecimal latestMonthMedianSalaryGrowthRate;
    private BigDecimal latestMonthUpperQuartileSalary;

    private BigDecimal latestYearAverageSalary;
    private BigDecimal latestYearMedianSalary;
    private BigDecimal latestYearAverageSalaryGrowthRate;
    private BigDecimal latestYearMedianSalaryGrowthRate;
    private BigDecimal latestYearUpperQuartileSalary;
}
