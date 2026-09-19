package com.training.urlshortener.repository;

import com.training.urlshortener.entity.UrlMapEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UrlRepositoryImpl implements UrlRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public UrlMapEntry getById(String id) {
        return mongoTemplate.findById(id, UrlMapEntry.class);
    }
}
