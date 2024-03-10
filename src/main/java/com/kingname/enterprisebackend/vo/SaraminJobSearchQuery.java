package com.kingname.enterprisebackend.vo;

import lombok.*;

import java.io.Serializable;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor @Builder
public class SaraminJobSearchQuery implements Serializable {

    private String keywords;
    private String loc_mcd;
    private String job_cd;
    private int count = 110;
}
