package com.intive.patronage.toz.service;

import com.intive.patronage.toz.exception.NotFoundException;
import com.intive.patronage.toz.exception.WrongEnumValueException;
import com.intive.patronage.toz.model.constant.PetValues;
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

    private final static String PET = "Pet";
    private final PetsRepository petsRepository;

    @Autowired
    PetsService(PetsRepository petsRepository) {
        this.petsRepository = petsRepository;
    }

    public List<PetView> findAllPets() {
        return convertList(petsRepository.findAll());
    }

    private List<PetView> convertList(List<Pet> pets) {
        List<PetView> petViews = new ArrayList<>();
        for (Pet pet : pets) {
            PetView petView = convertToView(pet);
            petViews.add(petView);
        }
        return petViews;
    }

    public PetView findById(final UUID id) {
        throwNotFoundExceptionIfNotExists(id);
        Pet pet = petsRepository.findOne(id);
        return convertToView(pet);
    }

    public PetView createPet(final PetView petView) {
        if (petView.getType() == null) {
            throw new WrongEnumValueException(PetValues.Type.class);
        }
        Pet pet = convertFromView(petView);
        return convertToView(petsRepository.save(pet));
    }

    private Pet convertFromView(final PetView petView) {
        return new Pet.Builder()
                .setName(petView.getName())
                .setType(petView.getType())
                .setSex(petView.getSex())
                .setDescription(petView.getDescription())
                .setAddress(petView.getAddress())
                .setCreated(petView.getCreated())
                .setLastModified(petView.getLastModified())
                .build();
    }

    public void deletePet(final UUID id) {
        throwNotFoundExceptionIfNotExists(id);
        petsRepository.delete(id);
    }

    public PetView updatePet(final UUID id, final PetView petView) {
        throwNotFoundExceptionIfNotExists(id);
        final Pet pet = new Pet.Builder(id)
                .setName(petView.getName())
                .setType(petView.getType())
                .setSex(petView.getSex())
                .setDescription(petView.getDescription())
                .setAddress(petView.getAddress())
                .setCreated(petView.getCreated())
                .setLastModified(petView.getLastModified())
                .build();
        return convertToView(petsRepository.save(pet));
    }

    private void throwNotFoundExceptionIfNotExists(final UUID id) {
        if (!petsRepository.exists(id)) {
            throw new NotFoundException(PET);
        }
    }

    private PetView convertToView(final Pet pet) {
        final PetView petView = new PetView();
        petView.setId(pet.getId());
        petView.setName(pet.getName());
        petView.setType(pet.getType());
        petView.setSex(pet.getSex());
        petView.setDescription(pet.getDescription());
        petView.setAddress(pet.getAddress());
        petView.setCreated(pet.getCreated());
        petView.setLastModified(pet.getLastModified());
        return petView;
    }
}
