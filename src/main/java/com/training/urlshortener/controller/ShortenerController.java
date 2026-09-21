package com.training.urlshortener.controller;

import com.training.urlshortener.constant.RequestExamples;
import com.training.urlshortener.constant.ResponseExamples;
import com.training.urlshortener.dto.ApiErrorResponse;
import com.training.urlshortener.dto.UrlRequest;
import com.training.urlshortener.dto.UrlResponse;
import com.training.urlshortener.service.UrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ShortenerController {

    private final UrlService urlService;

    @GetMapping(path = "/{id}")
    @Operation(
            summary = "Redirect to the corresponding registered long URL",
            description = """
                    This API redirects the client to the long URL registered
                    against this short URL ID.
                    """
    )
    @ApiResponse(
            responseCode = "302",
            description = "Registered URL found"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Registered URL not found",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(value = ResponseExamples.MISSING_URL)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "URL ID should be 7-character long",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(value = ResponseExamples.URL_ID_LENGTH_CONSTRAINT)
            )
    )
    ResponseEntity<Void> redirect(
            @PathVariable
            @Size(min = 7, max = 7, message = "The URL ID should be 7-character long")
            @Parameter(
                    description = "7-character long short URL ID",
                    example = "kl2c49e"
            )
            String id
    ) {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(urlService.getLongUrl(id))
                .build();
    }

    @PostMapping(path = "/v1/api/shorten")
    @Operation(
            summary = "Shorten a URL",
            description = """
                    Use this API to shorten a long URL
                    """,
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UrlRequest.class),
                            examples = @ExampleObject(value = RequestExamples.REGISTER_URL_REQUEST)
                    )
            )
    )
    @ApiResponse(
            responseCode = "200",
            description = "URL registered successfully",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UrlResponse.class),
                    examples = @ExampleObject(value = ResponseExamples.URL_REGISTERED)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "URL cannot be blank",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(value = ResponseExamples.URL_CANNOT_BE_BLANK)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "URL should be valid",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(value = ResponseExamples.URL_SHOULD_BE_VALID)
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "URL cannot exceed 2048 characters",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = @ExampleObject(value = ResponseExamples.URL_CANNOT_EXCEED_2048_CHARS)
            )
    )
    ResponseEntity<UrlResponse> getShortUrl(HttpServletRequest request, @Valid @RequestBody UrlRequest urlRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(urlService.registerUrl(request, urlRequest.toUri()));
    }
}
