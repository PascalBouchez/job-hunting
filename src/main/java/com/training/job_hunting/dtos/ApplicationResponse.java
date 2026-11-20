package com.training.job_hunting.dtos;

import com.training.job_hunting.models.Application;
import com.training.job_hunting.models.ApplicationStatus;

import java.time.LocalDate;
import java.util.List;

public record ApplicationResponse(
    Long id, String jobTitle, String jobDescription,
    Long monthlySalary, String location,
    String offerLink, LocalDate applicationDate,
    String notes, EnterpriseResponse enterprise,
    List<ReminderResponse> reminders,
    ApplicationStatus status
) {
    public static ApplicationResponse from(Application application) {
        return new ApplicationResponse(application.getId(), application.getJobTitle(),
                application.getJobDescription(), application.getMonthlySalary(),
                application.getLocation(), application.getOfferLink(),
                application.getApplicationDate(), application.getNotes(),
                EnterpriseResponse.from(application.getEnterprise()),
                application.getReminders().stream().map(ReminderResponse::from).toList(),
                application.getStatus()
        );
    }
}
