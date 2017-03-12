package com.intive.patronage.toz.service;

import com.intive.patronage.toz.exception.NotFoundException;
import com.intive.patronage.toz.exception.WrongEnumValueException;
import com.intive.patronage.toz.model.db.Pet;
import com.intive.patronage.toz.repository.PetsRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class PetsServiceTest {

    private static final String EXPECTED_NAME = "Johny";
    private static final Pet.Type EXPECTED_TYPE = Pet.Type.DOG;
    private Pet pet;
    private UUID petId;

    @Mock
    private PetsRepository petsRepository;
    private PetsService petsService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        petsService = new PetsService(petsRepository);
        pet = new Pet();
        pet.setName(EXPECTED_NAME);
        pet.setType(EXPECTED_TYPE);
        petId = pet.getId();
    }

    @Test
    public void findAllPets() throws Exception {
        when(petsRepository.findAll()).thenReturn(Collections.emptyList());

        List<Pet> pets = petsService.findAllPets();
        assertTrue(pets.isEmpty());
    }

    @Test
    public void findById() throws Exception {
        when(petsRepository.exists(petId)).thenReturn(true);
        when(petsRepository.findOne(petId)).thenReturn(pet);

        Pet dbPet = petsService.findById(petId);
        assertEquals(EXPECTED_NAME, dbPet.getName());

        verify(petsRepository, times(1)).exists(eq(petId));
        verify(petsRepository, times(1)).findOne(eq(petId));
        verifyNoMoreInteractions(petsRepository);
    }

    @Test(expected = NotFoundException.class)
    public void findByIdNotFoundException() throws Exception {
        when(petsRepository.exists(petId)).thenReturn(false);
        petsService.findById(petId);

        verify(petsRepository, times(1)).exists(eq(petId));
        verifyNoMoreInteractions(petsRepository);
    }

    @Test
    public void createPet() throws Exception {
        when(petsRepository.save(pet)).thenReturn(pet);

        Pet createdPet = petsService.createPet(pet);
        assertEquals(EXPECTED_NAME, createdPet.getName());
        assertEquals(EXPECTED_TYPE, createdPet.getType());

        verify(petsRepository, times(1)).save(eq(pet));
        verifyNoMoreInteractions(petsRepository);
    }

    @Test(expected = WrongEnumValueException.class)
    public void createPetWrongEnumValueException() throws Exception {
        pet.setType(null);
        when(petsRepository.exists(petId)).thenReturn(true);
        petsService.createPet(pet);

        verify(petsRepository, times(1)).exists(eq(petId));
        verifyNoMoreInteractions(petsRepository);
    }

    @Test
    public void deletePet() throws Exception {
        when(petsRepository.exists(petId)).thenReturn(true);
        petsService.deletePet(petId);

        verify(petsRepository, times(1)).exists(eq(petId));
        verify(petsRepository, times(1)).delete(eq(petId));
        verifyNoMoreInteractions(petsRepository);
    }

    @Test(expected = NotFoundException.class)
    public void deletePetNotFoundException() throws Exception {
        when(petsRepository.exists(petId)).thenReturn(false);
        petsService.deletePet(petId);

        verify(petsRepository, times(1)).exists(eq(petId));
        verifyNoMoreInteractions(petsRepository);
    }

    @Test
    public void updatePet() throws Exception {
        when(petsRepository.exists(petId)).thenReturn(true);
        when(petsRepository.save(pet)).thenReturn(pet);
        Pet savedPet = petsService.updatePet(petId, pet);

        assertEquals(EXPECTED_NAME, savedPet.getName());
        assertEquals(EXPECTED_TYPE, savedPet.getType());
    }

    @Test(expected = NotFoundException.class)
    public void updatePetNotFoundException() throws Exception {
        when(petsRepository.exists(petId)).thenReturn(false);
        petsService.updatePet(petId, pet);

        verify(petsRepository, times(1)).exists(eq(petId));
        verifyNoMoreInteractions(petsRepository);
    }

}