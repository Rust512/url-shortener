package com.training.urlshortener.dto;

import java.net.URI;

public record UrlResponse(
        URI longUrl,
        URI shortUrl
) {
}
