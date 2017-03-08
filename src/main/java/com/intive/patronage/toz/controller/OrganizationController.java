package com.intive.patronage.toz.controller;

import com.intive.patronage.toz.model.OrganizationInfo;
import com.intive.patronage.toz.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/organization", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrganizationController {

    private final OrganizationService service;

    @Autowired
    public OrganizationController(OrganizationService service) {
        this.service = service;
    }

    @GetMapping("/info")
    public ResponseEntity<OrganizationInfo> readOrganizationInfo() {
        OrganizationInfo info = service.findOrganizationInfo();
        return ResponseEntity.ok()
                .body(info);
    }

    @PostMapping(value = "/info", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrganizationInfo> createOrganizationInfo(@Valid @RequestBody OrganizationInfo info) {
        OrganizationInfo createdInfo = service.createOrganizationInfo(info);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdInfo);
    }

    @DeleteMapping("/info")
    public ResponseEntity<OrganizationInfo> deleteOrganizationInfo() {
        OrganizationInfo info = service.deleteOrganizationInfo();
        return ResponseEntity.ok()
                .body(info);
    }

    @PutMapping(value = "/info", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrganizationInfo> updateOrganizationInfo(@Valid @RequestBody OrganizationInfo info) {
        OrganizationInfo updatedInfo = service.updateOrganizationInfo(info);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(updatedInfo);
    }
}
