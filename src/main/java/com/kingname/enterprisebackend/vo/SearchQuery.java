package com.kingname.enterprisebackend.vo;

import lombok.*;

import java.util.List;

@Data @Builder
public class SearchQuery {

    @Getter @Setter @ToString
    @AllArgsConstructor @NoArgsConstructor @Builder
    public static class Request {
        private String date;
        private String year;
        private String month;
        private String location;
        private String industry;
        private String company;
        private String companyType;
        private String minMemberCount;
        private String maxMemberCount;
        private String csn;

        private int page;
        private int size;
        private String sort;
    }

    @Getter @Setter @ToString
    @AllArgsConstructor @NoArgsConstructor @Builder
    public static class Response {
        private int resultCnt;
        private List<?> resultList;
    }
}
