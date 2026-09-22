package com.training.url_shortener.config;

import com.training.url_shortener.dto.UrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.ObjectMapper;

@Configuration
@RequiredArgsConstructor
public class CacheConfig {

    private final ObjectMapper objectMapper;

    @Bean
    public RedisTemplate<String, UrlResponse> cacheRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, UrlResponse> template = new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory);

        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        JacksonJsonRedisSerializer<UrlResponse> jsonSerializer =
                new JacksonJsonRedisSerializer<>(objectMapper, UrlResponse.class);

        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(jsonSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(jsonSerializer);
        template.afterPropertiesSet();

        return template;
    }
}
