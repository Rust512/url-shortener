package com.training.url_shortener;

import org.springframework.boot.SpringApplication;

public class TestUrlShortenerApplication {
    static void main(String[] args) {
        SpringApplication.from(UrlShortenerApplication::main).with(TestcontainersConfiguration.class).run(args);
    }
}
