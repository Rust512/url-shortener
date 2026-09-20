package com.training.urlshortener.service;

import jakarta.servlet.http.HttpServletRequest;

import java.net.URI;

public interface UrlService {
    URI getLongUrl(String id);
    URI registerUrl(HttpServletRequest request, URI longUrl);
}
