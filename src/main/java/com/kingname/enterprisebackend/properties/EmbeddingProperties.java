package com.kingname.enterprisebackend.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "embedding")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor @ToString
public class EmbeddingProperties {

    private String url;
}
