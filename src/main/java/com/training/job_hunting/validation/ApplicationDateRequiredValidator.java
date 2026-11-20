package com.training.job_hunting.validation;

import com.training.job_hunting.dtos.ApplicationRequest;
import com.training.job_hunting.models.ApplicationStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ApplicationDateRequiredValidator implements ConstraintValidator<ApplicationDateRequiredWhenSent, ApplicationRequest> {

    @Override
    public boolean isValid(ApplicationRequest applicationRequest, ConstraintValidatorContext constraintValidatorContext) {
        boolean valid = applicationRequest.status() == null || applicationRequest.status() == ApplicationStatus.YET_TO_SEND_RESUME || applicationRequest.applicationDate() != null;

        if (!valid) {
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate(constraintValidatorContext.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("applicationDate")
                    .addConstraintViolation();
        }

        return valid;
    }
}
