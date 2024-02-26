package com.kingname.enterprisebackend.vo;

import lombok.*;

import java.util.List;

@Data @Builder
public class RecomCompany {

    @Getter @Setter @ToString
    @AllArgsConstructor @NoArgsConstructor @Builder
    public static class Request {
        private String year;
        private String month;
        private String location;
        private String industry;
        private String company;
        private String companyType;
    }

    @Getter @Setter @ToString
    @AllArgsConstructor @NoArgsConstructor @Builder
    public static class Response {
        private int resultCnt;
        private List<?> resultList;
    }
}
