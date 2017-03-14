package com.intive.patronage.toz.controller;

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
@RequestMapping(value = "/organization/info", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrganizationInfoController {

    private final OrganizationInfoService infoService;

    @Autowired
    OrganizationInfoController(OrganizationInfoService infoService) {
        this.infoService = infoService;
    }

    @ApiOperation("Get organization information")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Organization not found",
                    response = ControllerExceptionHandler.ErrorResponse.class)
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<OrganizationInfoView> readOrganizationInfo() {
        final OrganizationInfoView info = infoService.findOrganizationInfo();
        return ResponseEntity.ok()
                .body(info);
    }

    @ApiOperation("Create organization information")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 409, message = "Organization already exists",
                    response = ControllerExceptionHandler.ErrorResponse.class)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrganizationInfoView> createOrganizationInfo(@Valid @RequestBody OrganizationInfoView info) {
        final OrganizationInfoView createdInfo = infoService.createOrganizationInfo(info);
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();

        return ResponseEntity.created(location)
                .body(createdInfo);
    }

    @ApiOperation("Delete organization information")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Organization not found",
                    response = ControllerExceptionHandler.ErrorResponse.class)
    })
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<OrganizationInfoView> deleteOrganizationInfo() {
        final OrganizationInfoView info = infoService.deleteOrganizationInfo();
        return ResponseEntity.ok()
                .body(info);
    }

    @ApiOperation("Update organization information")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Organization not found",
                    response = ControllerExceptionHandler.ErrorResponse.class)
    })
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<OrganizationInfoView> updateOrganizationInfo(@Valid @RequestBody OrganizationInfoView info) {
        final OrganizationInfoView updatedInfo = infoService.updateOrganizationInfo(info);
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();

        return ResponseEntity.created(location)
                .body(updatedInfo);
    }
}
