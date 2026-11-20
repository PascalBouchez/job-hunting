package com.training.job_hunting.dtos;

import com.training.job_hunting.models.Enterprise;

public record EnterpriseResponse(Long id, String name, String headquarters) {
    public static EnterpriseResponse from(Enterprise enterprise) {
        return new EnterpriseResponse(
               enterprise.getId(), enterprise.getName(), enterprise.getHeadquarters());
    }
}

