package com.training.url_shortener.dto;

import java.net.URI;

public record UrlResponse(
        String id,
        URI longUrl,
        URI shortUrl
) {
}
