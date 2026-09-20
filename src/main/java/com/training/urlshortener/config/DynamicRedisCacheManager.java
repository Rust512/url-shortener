package com.training.urlshortener.config;

import com.training.urlshortener.exception.InvalidCacheNameException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.regex.Pattern;

@Slf4j
public class DynamicRedisCacheManager extends RedisCacheManager {
    private static final Pattern pattern = Pattern.compile("(?<name>\\w+):(?<duration>\\d+):(?<unit>NANOS|MICROS|MILLIS|SECONDS|MINUTES|HOURS|HALF_DAYS|DAYS|WEEKS|MONTHS|YEARS|DECADES|CENTURIES|MILLENNIA|ERAS|FOREVER)");

    public DynamicRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {
        super(cacheWriter, defaultCacheConfiguration);
    }

    @Override
    @NullMarked
    protected RedisCache createRedisCache(String name, @Nullable RedisCacheConfiguration configuration) {
        var matcher = pattern.matcher(name);
        if (!matcher.matches()) {
            log.warn("Cache registration failed; reason=invalid_cache_name");
            throw new InvalidCacheNameException(name);
        }

        String cacheName = matcher.group("name");
        int duration = Integer.parseInt(matcher.group("duration"));
        ChronoUnit durationUnit = ChronoUnit.valueOf(matcher.group("unit"));

        assert configuration != null;
        configuration.entryTtl(Duration.of(duration, durationUnit));

        return super.createRedisCache(cacheName, configuration);
    }
}
