package com.intive.patronage.toz.pet;

import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.pet.model.db.Pet;
import com.intive.patronage.toz.status.PetsStatusRepository;
import com.intive.patronage.toz.status.model.PetsStatus;
import com.intive.patronage.toz.util.RepositoryChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
class PetsService {

    private static final String PET = "Pet";
    private static final String PETS_STATUS = "Pets status";
    private final PetsRepository petsRepository;
    private final PetsStatusRepository petsStatusRepository;

    @Autowired
    PetsService(PetsRepository petsRepository, PetsStatusRepository petsStatusRepository) {
        this.petsRepository = petsRepository;
        this.petsStatusRepository = petsStatusRepository;
    }

    List<Pet> findAllPets() {
        return petsRepository.findAll();
    }

    List<Pet> findPetsWithFilledFields() {
        List<Pet> pets = petsRepository.findByNameNotNullAndTypeNotNullAndSexNotNull();
        List<Pet> publicPets = new ArrayList<>();
        pets.forEach(
                pet -> {
                    if (pet.getPetsStatus() != null) {
                        if (pet.getPetsStatus().isPublic()) {
                            publicPets.add(pet);
                        }
                    }
                }
        );
        return publicPets;
    }

    Pet findById(final UUID id) {
        RepositoryChecker.throwNotFoundExceptionIfNotExists(id, petsRepository, PET);
        return petsRepository.findOne(id);
    }

    Pet createPet(final Pet pet) {
        if (pet.getPetsStatus().getId() != null) {
            throwNotFoundExceptionIfStatusNotExists(pet.getPetsStatus().getId());
            PetsStatus petsStatus = petsStatusRepository.findOne(pet.getPetsStatus().getId());
            pet.setPetsStatus(petsStatus);
        } else {
            pet.setPetsStatus(null);
        }
        return petsRepository.save(pet);
    }

    void deletePet(final UUID id) {
        RepositoryChecker.throwNotFoundExceptionIfNotExists(id, petsRepository, PET);
        petsRepository.delete(id);
    }

    Pet updatePet(final UUID id, final Pet pet) {
        RepositoryChecker.throwNotFoundExceptionIfNotExists(id, petsRepository, PET);
        pet.setId(id);
        PetsStatus petsStatus;
        if (pet.getPetsStatus().getId() == null) {
            petsStatus = null;
        } else {
            petsStatus = petsStatusRepository.findOne(pet.getPetsStatus().getId());
        }
        pet.setPetsStatus(petsStatus);
        return petsRepository.save(pet);
    }

    void updatePetImageUrl(final UUID id, String imageUrl) {
        final Pet pet = petsRepository.findOne(id);
        if (pet == null) {
            throw new NotFoundException(PET);
        }
        pet.setImageUrl(imageUrl);
        updatePet(id, pet);
    }

    void throwNotFoundExceptionIfStatusNotExists(UUID id) {
        if (!petsStatusRepository.exists(id)) {
            throw new NotFoundException(PETS_STATUS);
        }
    }
}
