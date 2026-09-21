package com.training.url_shortener.constant;

public class ResponseExamples {
    private ResponseExamples() {
    }

    public static final String MISSING_URL = """
            {
              "statusCode": 404,
              "error": "Not Found",
              "exceptionName": "MissingEntryException",
              "message": "The requested URL (id=kl2c4p5) does not exist",
              "path": "/kl2c4p5",
              "timestamp": "2026-09-21T14:58:06.822022667Z"
            }
            """;

    public static final String URL_ID_LENGTH_CONSTRAINT = """
            {
              "statusCode": 400,
              "error": "Bad Request",
              "exceptionName": "HandlerMethodValidationException",
              "message": "400 BAD_REQUEST \\"Validation failure\\"",
              "path": "/abcd",
              "timestamp": "2026-09-21T15:39:10.803936547Z"
            }
            """;

    public static final String URL_REGISTERED = """
            {
              "longUrl": "https://start.spring.io/",
              "shortUrl": "http://localhost:8080/970WVh0"
            }
            """;

    public static final String URL_CANNOT_BE_BLANK = """
            {
              "statusCode": 400,
              "error": "Bad Request",
              "exceptionName": "MethodArgumentNotValidException",
              "message": "[The URL cannot be blank]",
              "path": "/v1/api/shorten",
              "timestamp": "2026-09-21T15:20:20.239010083Z"
            }
            """;

    public static final String URL_SHOULD_BE_VALID = """
            {
              "statusCode": 400,
              "error": "Bad Request",
              "exceptionName": "MethodArgumentNotValidException",
              "message": "[The URL should be valid]",
              "path": "/v1/api/shorten",
              "timestamp": "2026-09-21T15:32:49.255954038Z"
            }
            """;

    public static final String URL_CANNOT_EXCEED_2048_CHARS = """
            {
              "statusCode": 400,
              "error": "Bad Request",
              "exceptionName": "MethodArgumentNotValidException",
              "message": "[The URL cannot exceed 2048 characters]",
              "path": "/v1/api/shorten",
              "timestamp": "2026-09-21T15:36:28.947696600Z"
            }
            """;
}
