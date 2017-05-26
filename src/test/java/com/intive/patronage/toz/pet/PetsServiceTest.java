package com.intive.patronage.toz.pet;

import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.pet.model.db.Pet;
import com.intive.patronage.toz.status.PetsStatusRepository;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(DataProviderRunner.class)
public class PetsServiceTest {

    private static final String EXPECTED_NAME = "Johny";
    private static final Pet.Type EXPECTED_TYPE = Pet.Type.DOG;
    private static final Pet.Sex EXPECTED_SEX = Pet.Sex.MALE;

    @Mock
    private PetsRepository petsRepository;
    @Mock
    private PetsStatusRepository petsStatusRepository;
    private PetsService petsService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        petsService = new PetsService(petsRepository, petsStatusRepository);
    }

    @DataProvider
    public static Object[] getPet() {
        Pet pet = new Pet();
        pet.setName(EXPECTED_NAME);
        pet.setType(EXPECTED_TYPE);
        pet.setSex(EXPECTED_SEX);
        return new Pet[]{pet};
    }

    @Test
    public void findAllPets() throws Exception {
        when(petsRepository.findAll()).thenReturn(Collections.emptyList());

        List<Pet> pets = petsService.findAllPets();
        assertTrue(pets.isEmpty());
    }

    @Test
    @UseDataProvider("getPet")
    public void findById(final Pet pet) throws Exception {
        final UUID petId = pet.getId();
        when(petsRepository.exists(petId)).thenReturn(true);
        when(petsRepository.findOne(petId)).thenReturn(pet);

        Pet dbPet = petsService.findById(petId);
        assertEquals(EXPECTED_NAME, dbPet.getName());
        assertEquals(EXPECTED_TYPE, dbPet.getType());
        assertEquals(EXPECTED_SEX, dbPet.getSex());

        verify(petsRepository, times(1)).exists(eq(petId));
        verify(petsRepository, times(1)).findOne(eq(petId));
        verifyNoMoreInteractions(petsRepository);
    }

    @Test(expected = NotFoundException.class)
    public void findByIdNotFoundException() throws Exception {
        final UUID id = UUID.randomUUID();
        when(petsRepository.exists(id)).thenReturn(false);
        petsService.findById(id);

        verify(petsRepository, times(1)).exists(eq(id));
        verifyNoMoreInteractions(petsRepository);
    }

    @Test
    @UseDataProvider("getPet")
    public void createPet(final Pet pet) throws Exception {
        when(petsRepository.save(any(Pet.class))).thenReturn(pet);

        Pet createdPet = petsService.createPet(pet);
        assertEquals(EXPECTED_NAME, createdPet.getName());
        assertEquals(EXPECTED_TYPE, createdPet.getType());
        assertEquals(EXPECTED_SEX, createdPet.getSex());

        verify(petsRepository, times(1)).save(any(Pet.class));
        verifyNoMoreInteractions(petsRepository);
    }

    @Test
    public void deletePet() throws Exception {
        final UUID petId = UUID.randomUUID();
        when(petsRepository.exists(petId)).thenReturn(true);
        petsService.deletePet(petId);

        verify(petsRepository, times(1)).exists(eq(petId));
        verify(petsRepository, times(1)).delete(eq(petId));
        verifyNoMoreInteractions(petsRepository);
    }

    @Test(expected = NotFoundException.class)
    public void deletePetNotFoundException() throws Exception {
        final UUID petId = UUID.randomUUID();
        when(petsRepository.exists(petId)).thenReturn(false);
        petsService.deletePet(petId);

        verify(petsRepository, times(1)).exists(eq(petId));
        verifyNoMoreInteractions(petsRepository);
    }

    @Test
    @UseDataProvider("getPet")
    public void updatePet(final Pet pet) throws Exception {
        final UUID petId = pet.getId();
        when(petsRepository.findOne(petId)).thenReturn(pet);
        when(petsRepository.exists(petId)).thenReturn(true);
        when(petsRepository.save(any(Pet.class))).thenReturn(pet);
        Pet savedPet = petsService.updatePet(petId, pet);

        assertEquals(EXPECTED_NAME, savedPet.getName());
        assertEquals(EXPECTED_TYPE, savedPet.getType());
        assertEquals(EXPECTED_SEX, savedPet.getSex());
    }

    @Test(expected = NotFoundException.class)
    @UseDataProvider("getPet")
    public void updatePetNotFoundException(final Pet pet) throws Exception {
        final UUID petId = pet.getId();
        when(petsRepository.exists(petId)).thenReturn(false);
        petsService.updatePet(petId, pet);

        verify(petsRepository, times(1)).exists(eq(petId));
        verifyNoMoreInteractions(petsRepository);
    }

}
