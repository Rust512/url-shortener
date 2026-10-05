package com.training.url_shortener.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

import java.time.Duration;
import java.util.Collections;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    RedisCacheConfiguration defaultCacheConfiguration() {
        var typeValidator = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType("com.training.url_shortener.")
                .build();
        var jsonSerializer = GenericJacksonJsonRedisSerializer.builder()
                .enableDefaultTyping(typeValidator)
                .enableSpringCacheNullValueSupport()
                .build();
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5L))
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())
                )
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer)
                );
    }

    @Bean
    RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory, RedisCacheConfiguration defaultCacheConfiguration) {
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultCacheConfiguration)
                .transactionAware()
                .withInitialCacheConfigurations(Collections.singletonMap("urls", defaultCacheConfiguration))
                .build();
    }
}
