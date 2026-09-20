package com.training.urlshortener.annotation;

import java.lang.annotation.*;
import java.time.temporal.ChronoUnit;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DynamicTtlCacheable {
    String value();
    String key();
    long ttl();
    ChronoUnit timeUnit() default ChronoUnit.MINUTES;
}
