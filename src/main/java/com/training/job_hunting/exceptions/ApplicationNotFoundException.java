package com.training.job_hunting.exceptions;

public class ApplicationNotFoundException extends ResourceNotFoundException {
    public ApplicationNotFoundException(Long id) {
        super("Application", id);
    }
}
