package com.training.url_shortener.repository;

import com.training.url_shortener.entity.UrlMapEntry;

public interface UrlRepository {
    UrlMapEntry getById(String id);

    UrlMapEntry saveLongUrl(String longUrl);
}
