package com.intive.patronage.toz.service;

import com.intive.patronage.toz.model.Pet;
import com.intive.patronage.toz.repository.PetsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetsService {

    private PetsRepository repository;

    @Autowired
    public PetsService(PetsRepository repository) {
        this.repository = repository;
    }

    public List<Pet> getAll() {
        return repository.findAll();
    }

    public Pet getById(Long id) {
        return repository.findOne(id);
    }

    public Pet findByName(String name) {
        return repository.findByName(name);
    }

    public void add(Pet pet) {
        repository.save(pet);
    }

    public void update(Pet pet) {
        repository.save(pet);
    }

    public void delete(Pet pet) {
        repository.delete(pet);
    }

    public void delete(Long id) {
        repository.delete(id);
    }
}
