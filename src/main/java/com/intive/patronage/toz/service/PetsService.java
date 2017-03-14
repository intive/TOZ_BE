package com.intive.patronage.toz.service;

import com.intive.patronage.toz.exception.NotFoundException;
import com.intive.patronage.toz.exception.WrongEnumValueException;
import com.intive.patronage.toz.model.db.Pet;
import com.intive.patronage.toz.repository.PetsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PetsService {

    private final static String PET = "Pet";
    private PetsRepository petsRepository;

    @Autowired
    PetsService(PetsRepository petsRepository) {
        this.petsRepository = petsRepository;
    }

    public List<Pet> findAllPets() {
        return petsRepository.findAll();
    }

    public Pet findById(final UUID id) {
        throwNotFoundExceptionIfNotExists(id);
        return petsRepository.findOne(id);
    }

    public Pet createPet(final Pet pet) {
        if (pet.getType() == null) {
            throw new WrongEnumValueException(Pet.Type.class);
        }
        return petsRepository.save(pet);
    }

    public void deletePet(final UUID id) {
        throwNotFoundExceptionIfNotExists(id);
        petsRepository.delete(id);
    }

    public Pet updatePet(final UUID id, final Pet pet) {
        throwNotFoundExceptionIfNotExists(id);
        pet.setId(id);
        return petsRepository.save(pet);
    }

    private void throwNotFoundExceptionIfNotExists(final UUID id) {
        if (!petsRepository.exists(id)) {
            throw new NotFoundException(PET);
        }
    }
}
