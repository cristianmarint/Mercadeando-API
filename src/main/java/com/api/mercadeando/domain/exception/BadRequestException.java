package com.api.mercadeando.domain.exception;

public class BadRequestException extends Exception {
    public BadRequestException() {
    }

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(Long id, String type) {
        super(type + " with ID " + id + " was not found");
    }
}
