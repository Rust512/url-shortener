package com.training.url_shortener.service;

import com.training.url_shortener.annotation.DynamicTtlCacheable;
import com.training.url_shortener.dto.UrlResponse;
import com.training.url_shortener.entity.UrlMapEntry;
import com.training.url_shortener.exception.MissingEntryException;
import com.training.url_shortener.exception.SelfReferenceException;
import com.training.url_shortener.repository.UrlRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Strings;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;

    @Override
    @DynamicTtlCacheable(value = "url", key = "#id", ttl = 5L, timeUnit = ChronoUnit.MINUTES)
    public URI getLongUrl(String id) {
        var entry = urlRepository.getById(id);
        if (entry == null) {
            log.warn("URL fetch failed; reason=id_does_not_exist");
            throw new MissingEntryException(id);
        }

        return URI.create(entry.getLongUrl());
    }

    @Override
    @DynamicTtlCacheable(value = "url", key = "#id", ttl = 10L, timeUnit = ChronoUnit.MINUTES)
    public UrlResponse registerUrl(HttpServletRequest request, URI longUrl) {
        String appHost = request.getServerName();
        String urlHost = longUrl.getHost();

        if (Strings.CI.equals(appHost, urlHost)) {
            log.warn("URL registration failed; reason=app_referencing_url");
            throw new SelfReferenceException();
        }

        UrlMapEntry savedEntry = urlRepository.saveLongUrl(longUrl.toString());

        String scheme = request.getScheme();
        int port = request.getServerPort();

        var shortUrl = URI.create(String.format("%s://%s:%d/%s", scheme, appHost, port, savedEntry.getId()));
        return new UrlResponse(longUrl, shortUrl);
    }
}
