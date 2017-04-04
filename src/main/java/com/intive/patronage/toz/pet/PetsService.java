package com.intive.patronage.toz.pet;

import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.pet.model.db.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
class PetsService {

    private static final String PET = "Pet";
    private final PetsRepository petsRepository;

    @Autowired
    PetsService(PetsRepository petsRepository) {
        this.petsRepository = petsRepository;
    }

    List<Pet> findAllPets() {
        return petsRepository.findAll();
    }

    List<Pet> findPetsWithFilledFields() {
        return petsRepository.findByNameNotNullAndTypeNotNullAndSexNotNull();
    }

    Pet findById(final UUID id) {
        throwNotFoundExceptionIfNotExists(id);
        return petsRepository.findOne(id);
    }

    Pet createPet(final Pet pet) {
        return petsRepository.save(pet);
    }

    void deletePet(final UUID id) {
        throwNotFoundExceptionIfNotExists(id);
        petsRepository.delete(id);
    }

    Pet updatePet(final UUID id, final Pet pet) {
        throwNotFoundExceptionIfNotExists(id);
        pet.setId(id);
        return petsRepository.save(pet);
    }

    private void throwNotFoundExceptionIfNotExists(final UUID id) {
        if (!petsRepository.exists(id)) {
            throw new NotFoundException(PET);
        }
    }

    void updatePetImageUrl(final UUID id, String imageUrl) {
        throwNotFoundExceptionIfNotExists(id);
        final Pet pet = petsRepository.findOne(id);
        pet.setImageUrl(imageUrl);
        updatePet(id, pet);
    }
}
