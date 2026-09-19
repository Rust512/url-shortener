package com.training.urlshortener.service;

import java.net.URI;

public interface UrlService {
    URI getLongUrl(String id);
}
