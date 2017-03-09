package com.intive.patronage.toz.controller;

import com.intive.patronage.toz.model.Pet;
import com.intive.patronage.toz.service.PetsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/pets", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PetsController {

    private final PetsService service;

    @Autowired
    public PetsController(PetsService service) {
        this.service = service;
    }

    @GetMapping(value = "/")
    public ResponseEntity<List<Pet>> getAll() {
        List<Pet> pet = service.getAll();
        return ResponseEntity.ok(pet);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Pet> getById(@PathVariable Long id) {
        Pet pet = service.getById(id);
        return ResponseEntity.ok(pet);
    }

    @GetMapping
    public ResponseEntity<Pet> getPetByName(String name) {
        Pet pet = service.findByName(name);
        return ResponseEntity.ok(pet);
    }

    @PostMapping
    public ResponseEntity<?> addPet(Pet pet) {
        service.add(pet);
        return ResponseEntity.ok(pet);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deletePetById(@PathVariable Long id) {
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
