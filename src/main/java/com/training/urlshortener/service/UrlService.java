package com.training.urlshortener.service;

import com.training.urlshortener.dto.UrlResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.net.URI;

public interface UrlService {
    URI getLongUrl(String id);
    UrlResponse registerUrl(HttpServletRequest request, URI longUrl);
}
