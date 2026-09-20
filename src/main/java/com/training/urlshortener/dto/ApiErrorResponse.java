package com.training.urlshortener.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class ApiErrorResponse {
    private int statusCode;

    private String error;

    private String exceptionName;

    private String message;

    private String path;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Instant timestamp;
}
