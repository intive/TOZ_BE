package com.intive.patronage.toz.service;

import com.intive.patronage.toz.exception.NotFoundException;
import com.intive.patronage.toz.exception.WrongEnumValueException;
import com.intive.patronage.toz.model.constant.PetValues;
import com.intive.patronage.toz.model.db.Pet;
import com.intive.patronage.toz.model.view.PetView;
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
    private static final PetValues.Type EXPECTED_TYPE = PetValues.Type.DOG;
    private static final PetValues.Sex EXPECTED_SEX = PetValues.Sex.MALE;
    private PetView petView;
    private Pet petDb;
    private UUID petId;

    @Mock
    private PetsRepository petsRepository;
    private PetsService petsService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        petsService = new PetsService(petsRepository);
        petView = new PetView();
        petView.setName(EXPECTED_NAME);
        petView.setType(EXPECTED_TYPE);
        petView.setSex(EXPECTED_SEX);
        petId = petView.getId();

        petDb = new Pet();
        petDb.setName(EXPECTED_NAME);
        petDb.setType(EXPECTED_TYPE);
        petDb.setSex(EXPECTED_SEX);
    }

    @Test
    public void findAllPets() throws Exception {
        when(petsRepository.findAll()).thenReturn(Collections.emptyList());

        List<PetView> pets = petsService.findAllPets();
        assertTrue(pets.isEmpty());
    }

    @Test
    public void findById() throws Exception {
        when(petsRepository.exists(petId)).thenReturn(true);
        when(petsRepository.findOne(petId)).thenReturn(petDb);

        PetView dbPet = petsService.findById(petId);
        assertEquals(EXPECTED_NAME, dbPet.getName());
        assertEquals(EXPECTED_TYPE, dbPet.getType());
        assertEquals(EXPECTED_SEX, dbPet.getSex());

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
        when(petsRepository.save(any(Pet.class))).thenReturn(petDb);

        PetView createdPet = petsService.createPet(petView);
        assertEquals(EXPECTED_NAME, createdPet.getName());
        assertEquals(EXPECTED_TYPE, createdPet.getType());
        assertEquals(EXPECTED_SEX, createdPet.getSex());

        verify(petsRepository, times(1)).save(any(Pet.class));
        verifyNoMoreInteractions(petsRepository);
    }

    @Test(expected = WrongEnumValueException.class)
    public void createPetWrongEnumValueException() throws Exception {
        petView.setType(null);
        when(petsRepository.exists(petId)).thenReturn(true);
        petsService.createPet(petView);

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
        when(petsRepository.save(any(Pet.class))).thenReturn(petDb);
        PetView savedPet = petsService.updatePet(petId, petView);

        assertEquals(EXPECTED_NAME, savedPet.getName());
        assertEquals(EXPECTED_TYPE, savedPet.getType());
        assertEquals(EXPECTED_SEX, savedPet.getSex());
    }

    @Test(expected = NotFoundException.class)
    public void updatePetNotFoundException() throws Exception {
        when(petsRepository.exists(petId)).thenReturn(false);
        petsService.updatePet(petId, petView);

        verify(petsRepository, times(1)).exists(eq(petId));
        verifyNoMoreInteractions(petsRepository);
    }

}
