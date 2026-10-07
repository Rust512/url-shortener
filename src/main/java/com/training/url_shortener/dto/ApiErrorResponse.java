package com.training.url_shortener.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.Instant;

@Builder
public record ApiErrorResponse(
        int statusCode,
        String error,
        String exceptionName,
        String message,
        String path,
        @JsonFormat(shape = JsonFormat.Shape.STRING) Instant timestamp
) {
}
