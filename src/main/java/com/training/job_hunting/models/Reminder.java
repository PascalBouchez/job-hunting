package com.training.job_hunting.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate reminderDate;
    private String commentary;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ContactTypeForReminder contactType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Application application;

    public Long getId() {
        return id;
    }

    public LocalDate getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(LocalDate date) {
        this.reminderDate = date;
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public ContactTypeForReminder getContactType() {
        return contactType;
    }

    public void setContactType(ContactTypeForReminder contactType) {
        this.contactType = contactType;
    }
}
