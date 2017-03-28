package com.intive.patronage.toz.controller;

import com.intive.patronage.toz.error.ErrorResponse;
import com.intive.patronage.toz.error.ValidationErrorResponse;
import com.intive.patronage.toz.model.constant.ApiUrl;
import com.intive.patronage.toz.model.view.OrganizationInfoView;
import com.intive.patronage.toz.service.OrganizationInfoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = ApiUrl.ORGANIZATION_INFO_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
class OrganizationInfoController {

    private final OrganizationInfoService organizationInfoService;

    @Autowired
    OrganizationInfoController(OrganizationInfoService organizationInfoService) {
        this.organizationInfoService = organizationInfoService;
    }

    @ApiOperation("Get organization information")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Organization not found",
                    response = ErrorResponse.class)
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<OrganizationInfoView> readOrganizationInfo() {
        final OrganizationInfoView info = organizationInfoService.findOrganizationInfo();
        return ResponseEntity.ok()
                .body(info);
    }

    @ApiOperation("Create organization information")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request",
                    response = ValidationErrorResponse.class),
            @ApiResponse(code = 409, message = "Organization already exists",
                    response = ErrorResponse.class)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrganizationInfoView> createOrganizationInfo(@Valid @RequestBody OrganizationInfoView info) {
        final OrganizationInfoView createdInfo = organizationInfoService.createOrganizationInfo(info);
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();

        return ResponseEntity.created(location)
                .body(createdInfo);
    }

    @ApiOperation("Update organization information")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request",
                    response = ValidationErrorResponse.class),
            @ApiResponse(code = 404, message = "Organization not found",
                    response = ErrorResponse.class)
    })
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrganizationInfoView> updateOrganizationInfo(@Valid @RequestBody OrganizationInfoView info) {
        final OrganizationInfoView updatedInfo = organizationInfoService.updateOrganizationInfo(info);
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();

        return ResponseEntity.created(location)
                .body(updatedInfo);
    }
}
