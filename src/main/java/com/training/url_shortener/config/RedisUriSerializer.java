package com.training.url_shortener.config;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Component
public class RedisUriSerializer implements RedisSerializer<URI> {
    @Override
    @NullMarked
    public byte[] serialize(@Nullable URI value) throws SerializationException {
        if (value == null) {
            throw new SerializationException("URI cannot be null!");
        }

        return value.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    @Nullable
    public URI deserialize(byte @Nullable [] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        return URI.create(new String(bytes));
    }
}
