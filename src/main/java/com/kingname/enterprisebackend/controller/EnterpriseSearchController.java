package com.kingname.enterprisebackend.controller;

import com.kingname.enterprisebackend.service.EnterpriseSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin
@RequiredArgsConstructor
public class EnterpriseSearchController {

    private final EnterpriseSearchService enterpriseSearchService;

    @PostMapping("/company/list")
    public ResponseEntity<?> companyList() {
        log.info("POST /company/list");
        return ResponseEntity.ok("Company list");
    }

    @PostMapping("/industry/list")
    public ResponseEntity<?> industryCompanyList() {
        log.info("POST /industry/list");
        return ResponseEntity.ok("Industry list");
    }

    @PostMapping("/location/list")
    public ResponseEntity<?> locationCompanyList() {
        log.info("POST /location/list");
        return ResponseEntity.ok("Location list");
    }

    @PostMapping("/recruit/list")
    public ResponseEntity<?> recruitList() {
        log.info("POST /recruit/list");
        return ResponseEntity.ok("Recruit list");
    }
}
