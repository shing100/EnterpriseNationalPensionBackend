package com.kingname.enterprisebackend.vo;

import lombok.*;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor @Builder
public class SaraminJobSearchQuery {

    private String keywords;
    private String loc_mcd;
    private String job_cd;
    private int count;
}
