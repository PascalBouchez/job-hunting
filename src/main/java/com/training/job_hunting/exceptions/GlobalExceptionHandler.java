package com.training.job_hunting.exceptions;

import com.training.job_hunting.dtos.ValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception ex) {
        log.error("Unexpected error occurred", ex);
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred");
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers
            , HttpStatusCode status, WebRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Request contains invalid fields");
        List<ValidationError> validationErrors =  ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> new ValidationError(error.getField(),error.getCode(),error.getDefaultMessage()))
                .toList();
        problemDetail.setProperty("errors", validationErrors);
        return ResponseEntity.status(status).body(problemDetail);
    }

    @ExceptionHandler(InvalidReferenceException.class)
    public ProblemDetail handleInvalidReference(InvalidReferenceException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_CONTENT, "Request contains invalid references");
        problemDetail.setProperty("errors", List.of(
                new ValidationError(ex.getField(), "InvalidReference", ex.getMessage())
        ));
        return problemDetail;
    }

    @ExceptionHandler(InvalidSortOptionsException.class)
    public ProblemDetail handleInvalidSortOptions(InvalidSortOptionsException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Request contains invalid sort options");
        problemDetail.setProperty("errors", List.of(
                new ValidationError(ex.getParameter(), "InvalidSortOption", ex.getMessage())
        ));
        return problemDetail;
    }
}
