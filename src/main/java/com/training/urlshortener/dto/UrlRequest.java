package com.training.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.net.URI;

public record UrlRequest(
        @URL(message = "The URL should be valid")
        @NotBlank(message = "The URL cannot be blank")
        @Size(max = 2048, message = "The URL cannot exceed 2048 characters")
        String url
) {
    public URI toUri() {
        return URI.create(url);
    }
}
