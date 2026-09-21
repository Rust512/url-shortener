package com.training.urlshortener.constant;

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
}
