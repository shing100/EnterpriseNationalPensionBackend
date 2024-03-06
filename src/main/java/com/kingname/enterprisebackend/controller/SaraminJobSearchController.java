package com.kingname.enterprisebackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kingname.enterprisebackend.service.SaraminJobService;
import com.kingname.enterprisebackend.vo.SaraminJobSearchQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SaraminJobSearchController {

    private final SaraminJobService saraminJobService;
    private final ObjectMapper objectMapper;

    @CrossOrigin
    @GetMapping("/saramin/job-list")
    public ResponseEntity<?> searchJobs(@ModelAttribute SaraminJobSearchQuery query) throws UnsupportedEncodingException, JsonProcessingException {
        return ResponseEntity.ok(objectMapper.readValue(saraminJobService.searchJobs(query), Map.class));
    }
}
