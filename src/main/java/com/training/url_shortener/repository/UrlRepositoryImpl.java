package com.training.url_shortener.repository;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.training.url_shortener.entity.UrlMapEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UrlRepositoryImpl implements UrlRepository {
    private final MongoTemplate mongoTemplate;

    @Override
    public UrlMapEntry getById(String id) {
        return mongoTemplate.findById(id, UrlMapEntry.class);
    }

    @Override
    public UrlMapEntry saveLongUrl(String longUrl) {
        String id;

        do {
            id = NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, NanoIdUtils.DEFAULT_ALPHABET, 7);
        } while (idUsed(id));

        var entry = UrlMapEntry.builder()
                .id(id)
                .longUrl(longUrl)
                .build();

        return mongoTemplate.save(entry);
    }

    private boolean idUsed(String id) {
        var criteria = Criteria.where("_id").eq(id);
        return mongoTemplate.exists(Query.query(criteria), UrlMapEntry.class);
    }
}
