package com.training.job_hunting.controllers;

import com.training.job_hunting.dtos.ApplicationRequest;
import com.training.job_hunting.dtos.ApplicationResponse;
import com.training.job_hunting.models.ApplicationStatus;
import com.training.job_hunting.services.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }
    @GetMapping
    public PagedModel<ApplicationResponse> findByStatus(
            @RequestParam(required = false) ApplicationStatus status,
            @PageableDefault(size = 20, sort = "applicationDate", direction = Sort.Direction.DESC)Pageable pageable){
        return new PagedModel<>(applicationService.findByStatus(status, pageable));
    }

    @GetMapping("/{id}")
    public ApplicationResponse findById(@PathVariable Long id){
        return this.applicationService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse create(@Valid @RequestBody ApplicationRequest applicationRequest) {
        return this.applicationService.create(applicationRequest);
    }

    @PutMapping("/{id}")
    public ApplicationResponse update(@PathVariable Long id, @Valid @RequestBody ApplicationRequest applicationRequest) {
        return this.applicationService.update(id, applicationRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        this.applicationService.delete(id);
    }
}
