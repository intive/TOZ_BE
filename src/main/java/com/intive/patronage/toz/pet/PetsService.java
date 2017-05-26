package com.intive.patronage.toz.pet;

import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.pet.model.db.Pet;
import com.intive.patronage.toz.storage.model.db.UploadedFile;
import com.intive.patronage.toz.util.RepositoryChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
class PetsService {

    private static final String PET = "Pet";
    private static final String IMAGE = "Image";
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
        pet.setGallery(null);
        return petsRepository.save(pet);
    }

    void deletePet(final UUID id) {
        RepositoryChecker.throwNotFoundExceptionIfNotExists(id, petsRepository, PET);
        petsRepository.delete(id);
    }

    Pet updatePet(final UUID id, final Pet pet) {
        Pet oldPet = petsRepository.findOne(id);
        if (oldPet == null) {
            throw new NotFoundException(PET);
        } else {
            pet.setId(id);
            pet.setGallery(oldPet.getGallery());
            return petsRepository.save(pet);
        }
    }

    void updatePetImageUrl(final UUID id, String imageUrl) {
        final Pet pet = petsRepository.findOne(id);
        if (pet == null) {
            throw new NotFoundException(PET);
        }
        pet.setImageUrl(imageUrl);
        updatePet(id, pet);
    }

    void addImageToGallery(final UUID id, UploadedFile uploadedFile) {
        final Pet pet = petsRepository.findOne(id);
        if (pet == null) {
            throw new NotFoundException(PET);
        }
        pet.addToGallery(uploadedFile);
        updatePet(id, pet);
    }

    void removeImageFromGallery(final UUID id, UploadedFile uploadedFile) {
        final Pet pet = petsRepository.findOne(id);
        if (pet == null) {
            throw new NotFoundException(PET);
        }
        if (uploadedFile == null) {
            throw new NotFoundException(IMAGE);
        }
        pet.removeFromGallery(uploadedFile);
        updatePet(id, pet);
    }
}
