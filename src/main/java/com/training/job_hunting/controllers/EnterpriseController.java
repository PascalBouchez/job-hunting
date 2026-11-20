package com.training.job_hunting.controllers;

import com.training.job_hunting.dtos.EnterpriseRequest;
import com.training.job_hunting.dtos.EnterpriseResponse;
import com.training.job_hunting.services.EnterpriseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enterprises")
public class EnterpriseController {

    private final EnterpriseService enterpriseService;

    public EnterpriseController(EnterpriseService enterpriseService) {
        this.enterpriseService = enterpriseService;
    }

    @GetMapping()
    public List<EnterpriseResponse> findAll() {
        return this.enterpriseService.findAll();
    }

    @GetMapping("/{id}")
    public EnterpriseResponse findById(@PathVariable Long id) {
        return this.enterpriseService.findById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public EnterpriseResponse create(@Valid @RequestBody EnterpriseRequest enterpriseRequest) {
        return this.enterpriseService.create(enterpriseRequest);
    }

    @PutMapping("/{id}")
    public EnterpriseResponse update(@PathVariable Long id, @Valid @RequestBody EnterpriseRequest enterpriseRequest) {
        return this.enterpriseService.update(id, enterpriseRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        this.enterpriseService.delete(id);
    }


}
