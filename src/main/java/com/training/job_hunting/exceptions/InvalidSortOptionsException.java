package com.training.job_hunting.exceptions;

public class InvalidSortOptionsException extends RuntimeException {

    private final String parameter;

    public InvalidSortOptionsException(String parameter, String sortOption) {
        super("Invalid " + parameter + " parameter: " + sortOption);
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }
}
