package com.training.job_hunting.exceptions;

public class EnterpriseNotFoundException extends ResourceNotFoundException {
    public EnterpriseNotFoundException(Long id) {
        super("Entreprise", id);
    }
}
