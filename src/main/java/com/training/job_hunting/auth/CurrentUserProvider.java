package com.training.job_hunting.auth;

import org.springframework.stereotype.Component;

@Component
public class CurrentUserProvider {

    public Long getCurrentUserId() {
        return 1L; //TODO : va être remplacé plus tard par Spring Security
    }
}
