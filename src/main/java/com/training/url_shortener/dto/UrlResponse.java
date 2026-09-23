package com.training.url_shortener.dto;

import java.net.URI;

public record UrlResponse(
        URI longUrl,
        URI shortUrl
) {
}
