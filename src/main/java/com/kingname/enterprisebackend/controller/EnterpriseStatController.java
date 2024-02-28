package com.kingname.enterprisebackend.controller;

import com.kingname.enterprisebackend.exception.ErrorCode;
import com.kingname.enterprisebackend.exception.NationPensionException;
import com.kingname.enterprisebackend.service.EnterpriseSearchService;
import com.kingname.enterprisebackend.service.EnterpriseStatService;
import com.kingname.enterprisebackend.vo.AverageSalaryInfo;
import com.kingname.enterprisebackend.vo.SearchQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class EnterpriseStatController {

    private final EnterpriseSearchService enterpriseSearchService;
    private final EnterpriseStatService enterpriseStatService;

    @GetMapping("/company/contributions")
    public ResponseEntity<?> getTop5Contributions(@ModelAttribute SearchQuery.Request request) throws IOException {
        log.info("GET /company/contributions {}", request.toString());
        List<?> top5CompaniesByNationalPension = enterpriseStatService.getTopCompaniesByNationalPension(request.getSize());
        return ResponseEntity.ok(
                SearchQuery.Response.builder()
                        .resultCnt(top5CompaniesByNationalPension.size())
                        .resultList(top5CompaniesByNationalPension)
                        .build()
        );
    }

    @GetMapping("/recom/company")
    public ResponseEntity<SearchQuery.Response> getRecomCompany(@ModelAttribute SearchQuery.Request request) throws IOException {
        log.info("GET /recom/company {}", request.toString());
        List<?> enterpriseStatList = enterpriseSearchService.getEnterprishuffleList(request, request.getSize());
        return ResponseEntity.ok(
                SearchQuery.Response.builder()
                        .resultCnt(enterpriseStatList.size())
                        .resultList(enterpriseStatList)
                        .build()
        );
    }

    @GetMapping("/average/company")
    public ResponseEntity<?> averageStat() throws IOException {
        log.info("GET /average/company");
        try {
            Map<String, Object> averageSalaryInfo = enterpriseStatService.getAverageSalaryInfo();
            return ResponseEntity.ok(averageSalaryInfo);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NationPensionException(ErrorCode.INTERNAL_SERVER_EXCEPTION, "통계 조회 중 오류가 발생했습니다.");
        }
    }

    @GetMapping("/today/insight")
    public ResponseEntity<?> todayInsight() throws IOException {
        log.info("GET /today/insight");
        Map<String, Object> todayEnterpriseInsight = enterpriseStatService.getTodayEnterpriseInsight();
        return ResponseEntity.ok(todayEnterpriseInsight);
    }
}
