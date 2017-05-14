package com.intive.patronage.toz.mail;

import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.cache.GuavaTemplateCache;
import com.github.jknack.handlebars.cache.TemplateCache;
import com.github.jknack.handlebars.io.TemplateSource;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
class MailTemplateCache {

    private TemplateCache cache;

    @Autowired
    MailTemplateCache(@Value("${cache.durationInMinutes:10}") Integer cacheDurationInMinutes,
                      @Value("${cache.maxSize:100}") Integer cacheMaxSize) {
        final Cache<TemplateSource, Template> templateCache = CacheBuilder.newBuilder()
                .expireAfterWrite(cacheDurationInMinutes, TimeUnit.MINUTES)
                .maximumSize(cacheMaxSize)
                .build();

        cache = new GuavaTemplateCache(templateCache);
    }

    TemplateCache getCache() {
        return cache;
    }
}

