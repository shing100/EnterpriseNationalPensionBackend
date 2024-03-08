package com.kingname.enterprisebackend.service;

import com.kingname.enterprisebackend.exception.ErrorCode;
import com.kingname.enterprisebackend.exception.NationPensionException;
import com.kingname.enterprisebackend.vo.SaraminJobSearchQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaraminJobService {

    @Value("${saramin.api.key}")
    private String apiKey;

    @Value("${saramin.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    @Cacheable(value = "saraminJobList", key = "#query")
    public String searchJobs(SaraminJobSearchQuery query) throws UnsupportedEncodingException {
        try {
            String text = URLEncoder.encode(query.getKeywords() == null ? "" : query.getKeywords(), "UTF-8");
            String apiURL = "https://oapi.saramin.co.kr/job-search?access-key="+apiKey+"&keyword="+ text;
            if (query.getLoc_mcd() != null) {
                apiURL += "&loc_mcd=" + query.getLoc_mcd();
            }
            if (query.getJob_cd() != null) {
                apiURL += "&job_cd=" + query.getJob_cd();
            }
            apiURL += "&sr=directhire&count=100";

            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");

            int responseCode = con.getResponseCode();
            BufferedReader br;

            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            return response.toString();
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            throw new NationPensionException(ErrorCode.INTERNAL_SERVER_EXCEPTION);
        }
    }
}
