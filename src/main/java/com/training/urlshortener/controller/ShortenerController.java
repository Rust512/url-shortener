package com.training.urlshortener.controller;

import com.training.urlshortener.service.UrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/v1/api")
public class ShortenerController {

    private final UrlService urlService;

    @Cacheable(value = "urls", key = "#id")
    @GetMapping(path = "/{id}")
    ResponseEntity<Void> redirect(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(urlService.getLongUrl(id))
                .build();
    }
}
