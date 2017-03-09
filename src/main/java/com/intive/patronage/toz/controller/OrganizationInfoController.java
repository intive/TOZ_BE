package com.intive.patronage.toz.controller;

import com.intive.patronage.toz.model.view.OrganizationInfoView;
import com.intive.patronage.toz.service.OrganizationInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/organization/info", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrganizationInfoController {

    private final OrganizationInfoService infoService;

    @Autowired
    public OrganizationInfoController(OrganizationInfoService infoService) {
        this.infoService = infoService;
    }

    @GetMapping
    public ResponseEntity<OrganizationInfoView> readOrganizationInfo() {
        final OrganizationInfoView info = infoService.findOrganizationInfo();
        return ResponseEntity.ok()
                .body(info);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrganizationInfoView> createOrganizationInfo(@Valid @RequestBody OrganizationInfoView info) {
        final OrganizationInfoView createdInfo = infoService.createOrganizationInfo(info);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdInfo);
    }

    @DeleteMapping
    public ResponseEntity<OrganizationInfoView> deleteOrganizationInfo() {
        final OrganizationInfoView info = infoService.deleteOrganizationInfo();
        return ResponseEntity.ok()
                .body(info);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrganizationInfoView> updateOrganizationInfo(@Valid @RequestBody OrganizationInfoView info) {
        final OrganizationInfoView updatedInfo = infoService.updateOrganizationInfo(info);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(updatedInfo);
    }
}
