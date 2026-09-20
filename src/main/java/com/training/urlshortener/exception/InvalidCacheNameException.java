package com.training.urlshortener.exception;

public class InvalidCacheNameException extends RuntimeException {
    private static final String MESSAGE = """
            Cache name %s is invalid.
            Cache name should follow this convention:
            'name:duration:unit'
            where name -> name of the cache;
            number -> total TTL duration
            unit -> TTL duration unit (acceptable values are: NANOS, MICROS, MILLIS,
            SECONDS, MINUTES, HOURS, HALF_DAYS, DAYS, WEEKS, MONTHS, YEARS, DECADES, CENTURIES, MILLENNIA, ERAS, and FOREVER)
            Example: url:10:min
            Here, the cache name is 'url' and each entry will have a 10 minute TTL.
            """;

    public InvalidCacheNameException(String cacheName) {
        super(String.format(MESSAGE, cacheName));
    }
}
