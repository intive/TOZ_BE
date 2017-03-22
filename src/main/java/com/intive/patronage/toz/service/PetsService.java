package com.intive.patronage.toz.service;

import com.intive.patronage.toz.model.ModelMapper;
import com.intive.patronage.toz.exception.NotFoundException;
import com.intive.patronage.toz.model.db.Pet;
import com.intive.patronage.toz.model.view.PetView;
import com.intive.patronage.toz.repository.PetsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PetsService {

    private static final String PET = "Pet";
    private final PetsRepository petsRepository;

    @Autowired
    PetsService(PetsRepository petsRepository) {
        this.petsRepository = petsRepository;
    }

    public List<PetView> findAllPets() {
        List<PetView> petViews = new ArrayList<>();
        List<Pet> petDb = petsRepository.findByNameNotNullAndTypeNotNullAndSexNotNull();
        for (Pet pet : petDb) {
            PetView petView = convertToView(pet);
            petViews.add(petView);
        }
        return petViews;
    }

    private PetView convertToView(final Pet pet) {
        return ModelMapper.convertToView(pet, PetView.class);
    }

    public PetView findById(final UUID id) {
        throwNotFoundExceptionIfNotExists(id);
        Pet pet = petsRepository.findOne(id);
        return convertToView(pet);
    }

    public PetView createPet(final PetView petView) {
        Pet pet = convertFromView(petView);
        return convertToView(petsRepository.save(pet));
    }

    private Pet convertFromView(final PetView petView) {
        return ModelMapper.convertToModel(petView, Pet.class);
    }

    public void deletePet(final UUID id) {
        throwNotFoundExceptionIfNotExists(id);
        petsRepository.delete(id);
    }

    public PetView updatePet(final UUID id, final PetView petView) {
        throwNotFoundExceptionIfNotExists(id);
        final Pet pet = convertFromView(petView);
        pet.setId(id);
        return convertToView(petsRepository.save(pet));
    }

    private void throwNotFoundExceptionIfNotExists(final UUID id) {
        if (!petsRepository.exists(id)) {
            throw new NotFoundException(PET);
        }
    }

    public void updatePetImage(final UUID id, String imageUrl) {
        throwNotFoundExceptionIfNotExists(id);
        final Pet pet = petsRepository.findOne(id);
        pet.setImageUrl(imageUrl);
        updatePet(id, convertToView(pet));
    }
}
