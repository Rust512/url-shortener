package com.training.urlshortener.controller;

import com.training.urlshortener.dto.UrlRequest;
import com.training.urlshortener.dto.UrlResponse;
import com.training.urlshortener.service.UrlService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class ShortenerController {

    private final UrlService urlService;

    @GetMapping(path = "/{id}")
    ResponseEntity<Void> redirect(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(urlService.getLongUrl(id))
                .build();
    }

    @PostMapping(path = "/v1/api/shorten")
    ResponseEntity<UrlResponse> getShortUrl(HttpServletRequest request, @RequestBody UrlRequest urlRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(urlService.registerUrl(request, urlRequest.toUri()));
    }
}
