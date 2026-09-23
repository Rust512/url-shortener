package com.training.url_shortener.service;

import com.training.url_shortener.dto.UrlResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.net.URI;

public interface UrlService {
    URI getLongUrl(String id);

    UrlResponse registerUrl(HttpServletRequest request, URI longUrl);
}
