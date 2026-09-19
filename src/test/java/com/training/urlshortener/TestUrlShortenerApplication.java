package com.training.urlshortener;

import org.springframework.boot.SpringApplication;

public class TestUrlShortenerApplication {
    static void main(String[] args) {
        SpringApplication.from(UrlShortenerApplication::main).with(TestcontainersConfiguration.class).run(args);
    }
}
