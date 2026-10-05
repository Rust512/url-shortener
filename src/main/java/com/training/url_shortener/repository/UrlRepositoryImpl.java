package com.training.url_shortener.repository;

import com.training.url_shortener.entity.UrlMapEntry;
import com.training.url_shortener.generator.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.net.URI;

@Repository
@RequiredArgsConstructor
public class UrlRepositoryImpl implements UrlRepository {
    private final MongoTemplate mongoTemplate;
    private final IdGenerator idGenerator;

    @Override
    @Cacheable(value = "urls", key = "#id")
    public UrlMapEntry getById(String id) {
        return mongoTemplate.findById(id, UrlMapEntry.class);
    }

    @Override
    @CachePut(value = "urls", key = "#result.id")
    public UrlMapEntry saveLongUrl(String longUrl) {
        String id;

        do {
            id = idGenerator.generateId();
        } while (idUsed(id));

        var entry = UrlMapEntry.builder()
                .id(id)
                .longUrl(URI.create(longUrl))
                .build();

        return mongoTemplate.save(entry);
    }

    private boolean idUsed(String id) {
        var criteria = Criteria.where("_id").eq(id);
        return mongoTemplate.exists(Query.query(criteria), UrlMapEntry.class);
    }
}
