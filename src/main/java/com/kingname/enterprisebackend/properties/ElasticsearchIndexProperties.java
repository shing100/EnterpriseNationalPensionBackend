package com.kingname.enterprisebackend.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "elasticsearch.index")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class ElasticsearchIndexProperties {
    private String nationalPensionCollectIndex;
    private String nationalPensionCollectAlias;
    private String nationalPensionIndustryCollectIndex;
    private String nationalPensionLocationCollectIndex;
}
