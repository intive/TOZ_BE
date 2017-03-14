package com.intive.patronage.toz.controller;

import com.intive.patronage.toz.model.constant.PetsConstants;
import com.intive.patronage.toz.model.db.Pet;
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
@RequestMapping(value = PetsConstants.PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class PetsController {

    private final PetsService petsService;

    @Autowired
    PetsController(PetsService service) {
        this.petsService = service;
    }

    @ApiOperation("Get all pets")
    @GetMapping
    public List<Pet> getAllPets() {
        return petsService.findAllPets();
    }

    @ApiOperation("Get single pet by id")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Pet not found",
                    response = ControllerExceptionHandler.ErrorResponse.class)
    })
    @GetMapping(value = "/{id}")
    public Pet getPetById(@ApiParam(required = true) @PathVariable UUID id) {
        return petsService.findById(id);
    }

    @ApiOperation("Create new pet")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Pet not found",
                    response = ControllerExceptionHandler.ErrorResponse.class)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createPet(@Valid @RequestBody Pet pet) {
        Pet createdPet = petsService.createPet(pet);
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
                    response = ControllerExceptionHandler.ErrorResponse.class)
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Pet> deletePetById(@PathVariable UUID id) {
        petsService.deletePet(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("Update pet information")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Pet not found",
                    response = ControllerExceptionHandler.ErrorResponse.class)
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Pet> updatePet(@PathVariable UUID id, @RequestBody Pet pet) {
        final Pet updatedPet = petsService.updatePet(id, pet);
        return ResponseEntity.ok(updatedPet);
    }
}
