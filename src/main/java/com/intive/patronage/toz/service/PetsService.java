package com.intive.patronage.toz.service;

import com.intive.patronage.toz.exception.NotFoundException;
import com.intive.patronage.toz.model.db.Pet;
import com.intive.patronage.toz.repository.PetsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetsService {

    private final static String PET = "Pet";
    private PetsRepository repository;

    @Autowired
    public PetsService(PetsRepository repository) {
        this.repository = repository;
    }

    public List<Pet> getAll() {
        return repository.findAll();
    }

    public Pet findById(final Long id) {
        throwNotFoundExceptionIfNotExists(id);
        return repository.findOne(id);
    }

    public Pet findByName(String name) {
        return repository.findByName(name);
    }

    public void createPet(final Pet pet) {
        repository.save(pet);
    }

    public void deletePet(final Long id) {
        throwNotFoundExceptionIfNotExists(id);
        repository.delete(id);
    }

    public Pet updatePet(final Pet pet) {
        final Long id = pet.getId();
        throwNotFoundExceptionIfNotExists(id);
        return repository.save(pet);
    }

    private void throwNotFoundExceptionIfNotExists(final Long id) {
        if (!repository.exists(id)) {
            throw new NotFoundException(PET);
        }
    }
}
