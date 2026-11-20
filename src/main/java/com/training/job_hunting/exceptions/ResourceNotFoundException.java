package com.training.job_hunting.exceptions;


public abstract class ResourceNotFoundException extends RuntimeException {
    protected ResourceNotFoundException(String resourceName, Long id) {
        super(resourceName + " with id " + id + " not found");
    }
}
