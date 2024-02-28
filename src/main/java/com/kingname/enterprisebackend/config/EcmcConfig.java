package com.kingname.enterprisebackend.config;

import com.kingname.embeddingstoremanager.EmbeddingCacheManager;
import com.kingname.embeddingstoremanager.EmbeddingCacheManagerConfig;
import com.kingname.embeddingstoremanager.exception.EmbeddingCacheManagerException;
import com.kingname.enterprisebackend.properties.ElasticsearchProperties;
import com.kingname.enterprisebackend.properties.EmbeddingProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
@RequiredArgsConstructor
public class EcmcConfig {

    private final ElasticsearchProperties elasticsearchProperties;
    private final EmbeddingProperties embeddingProperties;

    @Bean
    public EmbeddingCacheManager getEmbeddingCacheManager() throws EmbeddingCacheManagerException {
        return new EmbeddingCacheManager(embeddingCacheManagerConfig());
    }

    @Bean
    public EmbeddingCacheManagerConfig embeddingCacheManagerConfig() throws EmbeddingCacheManagerException {
        return EmbeddingCacheManagerConfig.builder()
                .elasticSearchCacheHosts(elasticsearchProperties.getHosts())
                .elasticSearchCachePort(elasticsearchProperties.getPort())
                .elasticSearchCacheAliasName("embedding_cache_store")
                .modelName("embedding_onnx_int8_model")
                .embeddingApiUrl(embeddingProperties.getUrl())
                .build();
    }
}
