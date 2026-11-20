package com.training.job_hunting.exceptions;

public class InvalidReferenceException extends RuntimeException {

    private final String field;

    public InvalidReferenceException(String field, Long id) {
        super("Invalid reference for field " + field + " with id " + id);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
