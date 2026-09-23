package com.training.url_shortener.cache;

import com.training.url_shortener.config.RedisUriSerializer;
import com.training.url_shortener.dto.UrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.net.URI;

@Configuration
@RequiredArgsConstructor
public class CacheConfig {

    @Bean
    RedisTemplate<String, URI> cacheRedisTemplate(RedisConnectionFactory connectionFactory, RedisUriSerializer redisUriSerializer) {
        RedisTemplate<String, URI> template = new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory);

        StringRedisSerializer stringSerializer = new StringRedisSerializer();

        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(redisUriSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(redisUriSerializer);
        template.afterPropertiesSet();

        return template;
    }
}
