package com.intive.patronage.toz.controller;

import com.intive.patronage.toz.error.ErrorResponse;
import com.intive.patronage.toz.model.constant.PetValues;
import com.intive.patronage.toz.model.request.PetRequestBody;
import com.intive.patronage.toz.model.view.PetView;
import com.intive.patronage.toz.service.PetsService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Api(value = "Pet", description = "Operations for pet resources")
@RestController
@RequestMapping(value = PetValues.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
class PetsController {

    private final PetsService petsService;

    @Autowired
    PetsController(PetsService service) {
        this.petsService = service;
    }

    @ApiOperation("Get all pets")
    @GetMapping
    public List<PetView> getAllPets() {
        return petsService.findAllPets();
    }

    @ApiOperation(value = "Get single pet by id")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Pet not found",
                    response = ErrorResponse.class),
    })
    @GetMapping(value = "/{id}")
    public PetView getPetById(@ApiParam(required = true) @PathVariable UUID id) {
        return petsService.findById(id);
    }

    @ApiOperation(value = "Create new pet", response = PetView.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Pet not found",
                    response = ErrorResponse.class)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PetView> createPet(@Valid @RequestBody PetRequestBody pet) {
        PetView createdPet = petsService.createPet(pet);
        final URI baseLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        String petLocationString = String.format("%s%s", baseLocation, createdPet.getId());
        URI location = UriComponentsBuilder.fromUriString(petLocationString).build().toUri();
        return ResponseEntity.created(location)
                .body(createdPet);
    }

    @ApiOperation("Delete pet")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Pet not found",
                    response = ErrorResponse.class)
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<PetView> deletePetById(@PathVariable UUID id) {
        petsService.deletePet(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Update pet information", response = PetView.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Pet not found",
                    response = ErrorResponse.class)
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public PetView updatePet(@PathVariable UUID id, @RequestBody PetRequestBody pet) {
        return petsService.updatePet(id, pet);
    }
}
