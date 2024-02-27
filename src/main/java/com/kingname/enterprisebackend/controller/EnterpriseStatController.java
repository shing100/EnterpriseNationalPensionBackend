package com.kingname.enterprisebackend.controller;

import com.kingname.enterprisebackend.service.EnterpriseStatService;
import com.kingname.enterprisebackend.vo.SearchQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EnterpriseStatController {

    private final EnterpriseStatService enterpriseStatService;

    @GetMapping("/recom/company")
    public ResponseEntity<SearchQuery.Response> getRecomCompany(@ModelAttribute SearchQuery.Request request) throws IOException {
        log.info("GET /recom/company {}", request.toString());

        List<?> enterpriseStatList = enterpriseStatService.getEnterpriseLocationList(request);
        return ResponseEntity.ok(
                SearchQuery.Response.builder()
                        .resultCnt(0)
                        .resultList(enterpriseStatList)
                        .build()
        );
    }

    @GetMapping("/average/company")
    public ResponseEntity<?> averageStat() {
        log.info("GET /average-stat");
        return ResponseEntity.ok("Average Stat");
    }
}
