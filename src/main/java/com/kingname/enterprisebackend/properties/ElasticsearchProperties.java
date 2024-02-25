package com.kingname.enterprisebackend.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "elasticsearch")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor @ToString
public class ElasticsearchProperties {
    private List<String> hosts;
    private Integer port;
}
