package com.kingname.enterprisebackend.component;

import lombok.extern.slf4j.Slf4j;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;

@Slf4j
public class JobSearchCacheEventListener implements CacheEventListener {
    @Override
    public void onEvent(CacheEvent event) {
        log.debug("cache event logger message. getKey: {} / getOldValue: {} / getNewValue: {}",
                event.getKey(), event.getOldValue(), event.getNewValue());
    }
}
