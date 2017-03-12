package com.intive.patronage.toz.controller;

import com.intive.patronage.toz.model.db.Pet;
import com.intive.patronage.toz.service.PetsService;
import io.swagger.annotations.ApiOperation;
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

@RestController
@RequestMapping(value = "/pets", produces = MediaType.APPLICATION_JSON_VALUE)
public class PetsController {

    private final PetsService petsService;

    @Autowired
    PetsController(PetsService service) {
        this.petsService = service;
    }

    @ApiOperation("Get all pets")
    @GetMapping
    public ResponseEntity<List<Pet>> getAllPets() {
        final List<Pet> pet = petsService.findAllPets();
        return ResponseEntity.ok(pet);
    }

    @ApiOperation("Get single pet by id")
    @GetMapping(value = "/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable UUID id) {
        final Pet pet = petsService.findById(id);
        return ResponseEntity.ok(pet);
    }

    @ApiOperation("Create new pet")
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
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Pet> deletePetById(@PathVariable UUID id) {
        petsService.deletePet(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation("Update pet information")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Pet> updatePet(@PathVariable UUID id, @RequestBody Pet pet) {
        final Pet updatedPet = petsService.updatePet(id, pet);
        return ResponseEntity.ok(updatedPet);
    }
}
