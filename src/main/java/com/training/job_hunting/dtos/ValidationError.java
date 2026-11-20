package com.training.job_hunting.dtos;

public record ValidationError(String field, String code, String message) {
}
