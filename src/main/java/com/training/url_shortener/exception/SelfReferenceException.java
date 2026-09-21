package com.training.url_shortener.exception;

public class SelfReferenceException extends RuntimeException {
    private static final String MESSAGE = "The given URL references to this app";

    public SelfReferenceException() {
        super(MESSAGE);
    }
}
