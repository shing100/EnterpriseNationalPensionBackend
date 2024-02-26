package com.kingname.enterprisebackend.service;

import com.kingname.enterprisebackend.properties.ElasticsearchIndexProperties;
import com.kingname.enterprisebackend.vo.AverageSalaryInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EnterpriseStatService {

    private final EnterpriseSearchService enterpriseSearchService;
    private final ElasticsearchIndexProperties elasticsearchIndexProperties;

    public List<?> getEnterpriseLocationList() throws IOException {
        List<Map> nationalPensionLocationSearchList = enterpriseSearchService.getNationalPensionLocationSearchList();
        log.info("{}", nationalPensionLocationSearchList);
        return nationalPensionLocationSearchList;
    }

    // 오늘의 인사이트 (기업 총 수, 전체 근로자 수, 취업자수, 실업자수)
    public List<?> getTodayEnterpriseInsight() {
        return null;
    }

    // 평균연봉정보 (이번달 평균, 중위 연봉, 최신년도 평균 연봉, 중위 연봉)
    public AverageSalaryInfo getAverageSalaryInfo() {
        return null;
    }

    // 국민연금 납부액 TOP 5 기업
    public List<?> getTop5CompaniesByNationalPension() {
        return null;
    }
}
