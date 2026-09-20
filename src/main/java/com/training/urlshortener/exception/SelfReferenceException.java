package com.training.urlshortener.exception;

public class SelfReferenceException extends RuntimeException {
    private static final String MESSAGE = "The given URL references to this app";

    public SelfReferenceException() {
        super(MESSAGE);
    }
}
