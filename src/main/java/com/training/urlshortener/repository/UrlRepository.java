package com.training.urlshortener.repository;

import com.training.urlshortener.entity.UrlMapEntry;

public interface UrlRepository {
    UrlMapEntry getById(String id);

    UrlMapEntry saveLongUrl(String longUrl);
}
