package com.training.urlshortener.exception;

public class MissingEntryException extends RuntimeException {
    private static final String MESSAGE = "The requested URL (id=%s) does not exist";

    public MissingEntryException(String id) {
        super(String.format(MESSAGE, id));
    }
}
