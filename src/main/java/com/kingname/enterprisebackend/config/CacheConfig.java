package com.kingname.enterprisebackend.config;


import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.Caching;
import java.net.URISyntaxException;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() throws URISyntaxException {
        JCacheCacheManager jCacheCacheManager = new JCacheCacheManager(Caching.getCachingProvider().getCacheManager(
                getClass().getResource("/Ehcache.xml").toURI(),
                getClass().getClassLoader()
        ));
        javax.cache.CacheManager cacheManager = jCacheCacheManager.getCacheManager();
        return jCacheCacheManager;
    }
}
