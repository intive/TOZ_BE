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

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/pets",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE)
public class PetsController {

    private final PetsService petsService;

    @Autowired
    public PetsController(PetsService service) {
        this.petsService = service;
    }

    @ApiOperation("Get all pets")
    @GetMapping
    public ResponseEntity<List<Pet>> getAllPets() {
        final List<Pet> pet = petsService.getAll();
        return ResponseEntity.ok(pet);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long id) {
        final Pet pet = petsService.findById(id);
        return ResponseEntity.ok(pet);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Pet> createPet(@RequestBody Pet pet) {
        petsService.createPet(pet);
        return ResponseEntity.ok(pet);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Pet> deletePetById(@PathVariable Long id) {
        petsService.deletePet(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Pet> updatePet(@RequestBody Pet pet) {
        final Pet updatedPet = petsService.updatePet(pet);
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        return ResponseEntity.created(location)
                .body(updatedPet);
    }
}
