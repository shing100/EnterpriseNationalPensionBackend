package com.kingname.enterprisebackend.controller;

import com.kingname.enterprisebackend.service.EnterpriseSearchService;
import com.kingname.enterprisebackend.vo.CompanyDetail;
import com.kingname.enterprisebackend.vo.IndustryStatistic;
import com.kingname.enterprisebackend.vo.LocationStatistic;
import com.kingname.enterprisebackend.vo.SearchQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@CrossOrigin
@RequiredArgsConstructor
public class EnterpriseSearchController {

    private final EnterpriseSearchService enterpriseSearchService;

    @PostMapping("/company/list")
    public ResponseEntity<?> companyList(@ModelAttribute SearchQuery.Request request) throws IOException {
        log.info("POST /company/list");
        List<CompanyDetail> enterpriseSearchList = enterpriseSearchService.getEnterpriseSearchList(request);
        return ResponseEntity.ok(
                SearchQuery.Response.builder()
                .resultCnt(enterpriseSearchList.size())
                .resultList(enterpriseSearchList)
                .build()
        );
    }

    @PostMapping("/industry/list")
    public ResponseEntity<?> industryCompanyList(@ModelAttribute SearchQuery.Request request) throws IOException {
        log.info("POST /industry/list {}", request.toString());
        List<IndustryStatistic> enterpriseIndustryList = enterpriseSearchService.getEnterpriseIndustryList(request);
        return ResponseEntity.ok(
                SearchQuery.Response.builder()
                        .resultCnt(enterpriseIndustryList.size())
                        .resultList(enterpriseIndustryList)
                        .build()
        );
    }

    @PostMapping("/location/list")
    public ResponseEntity<?> locationCompanyList(@ModelAttribute SearchQuery.Request request) throws IOException {
        log.info("POST /location/list {}", request.toString());
        List<LocationStatistic> enterpriseLocationList = enterpriseSearchService.getEnterpriseLocationList(request);
        return ResponseEntity.ok(
                SearchQuery.Response.builder()
                        .resultCnt(enterpriseLocationList.size())
                        .resultList(enterpriseLocationList)
                        .build()
        );
    }

}
