package com.intive.patronage.toz.pet;

import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.pet.model.db.Pet;
import com.intive.patronage.toz.status.PetsStatusRepository;
import com.intive.patronage.toz.status.model.PetStatus;
import com.intive.patronage.toz.storage.model.db.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.intive.patronage.toz.util.RepositoryChecker.throwNotFoundExceptionIfNotExists;

@Service
class PetsService {

    private static final String PET = "Pet";
    private static final String IMAGE = "Image";
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
                    if (pet.getPetStatus() != null) {
                        if (pet.getPetStatus().isPublic()) {
                            publicPets.add(pet);
                        }
                    } else { //TODO: remove this 'else' when mobile clients finish testing
                        publicPets.add(pet);
                    }
                }
        );
        return publicPets;
    }

    Pet findById(final UUID id) {
        throwNotFoundExceptionIfNotExists(id, petsRepository, PET);
        return petsRepository.findOne(id);
    }

    Pet createPet(final Pet pet) {
        pet.setGallery(null);
        if (pet.getPetStatus() != null) {
            UUID petStatusId = pet.getPetStatus().getId();
            throwNotFoundExceptionIfNotExists(petStatusId, petsStatusRepository, PETS_STATUS);
            PetStatus petStatus = petsStatusRepository.findOne(petStatusId);
            pet.setPetStatus(petStatus);
        } else {
            pet.setPetStatus(null);
        }
        return petsRepository.save(pet);
    }

    void deletePet(final UUID id) {
        throwNotFoundExceptionIfNotExists(id, petsRepository, PET);
        petsRepository.delete(id);
    }

    Pet updatePet(final UUID id, final Pet pet) {
        throwNotFoundExceptionIfNotExists(id, petsRepository, PET);
        pet.setId(id);
        pet.setGallery(petsRepository.findOne(id).getGallery());
        if (pet.getPetStatus() != null) {
            UUID updatePetStatusId = pet.getPetStatus().getId();
            throwNotFoundExceptionIfNotExists(updatePetStatusId, petsStatusRepository, PETS_STATUS);
            PetStatus petStatus = petsStatusRepository.findOne(updatePetStatusId);
            pet.setPetStatus(petStatus);
        }
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
