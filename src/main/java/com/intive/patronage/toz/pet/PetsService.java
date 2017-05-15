package com.intive.patronage.toz.pet;

import com.intive.patronage.toz.pet.model.db.Pet;
import com.intive.patronage.toz.util.RepositoryChecker;
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
        RepositoryChecker.throwNotFoundExceptionIfNotExists(id, petsRepository, PET);
        return petsRepository.findOne(id);
    }

    Pet createPet(final Pet pet) {
        return petsRepository.save(pet);
    }

    void deletePet(final UUID id) {
        RepositoryChecker.throwNotFoundExceptionIfNotExists(id, petsRepository, PET);
        petsRepository.delete(id);
    }

    Pet updatePet(final UUID id, final Pet pet) {
        RepositoryChecker.throwNotFoundExceptionIfNotExists(id, petsRepository, PET);
        pet.setId(id);
        return petsRepository.save(pet);
    }

    void updatePetImageUrl(final UUID id, String imageUrl) {
        RepositoryChecker.throwNotFoundExceptionIfNotExists(id, petsRepository, PET);
        final Pet pet = petsRepository.findOne(id);
        pet.setImageUrl(imageUrl);
        updatePet(id, pet);
    }
}
