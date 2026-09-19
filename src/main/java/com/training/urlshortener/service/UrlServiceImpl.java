package com.training.urlshortener.service;

import com.training.urlshortener.repository.UrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class UrlServiceImpl implements UrlService {

    private final UrlRepository urlRepository;

    @Override
    public URI getLongUrl(String id) {
        return URI.create(urlRepository.getById(id).getLongUrl());
    }
}
