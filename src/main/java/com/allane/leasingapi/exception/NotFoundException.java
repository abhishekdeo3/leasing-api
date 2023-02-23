package com.allane.leasingapi.exception;

public class NotFoundException extends RuntimeException {

    public static final String MESSAGE_TEMPLATE = "Not Found with %s ID: %s";

    public NotFoundException(String message, long id) {
        super(String.format(MESSAGE_TEMPLATE, message, id));
    }
}
