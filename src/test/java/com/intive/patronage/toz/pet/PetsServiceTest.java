package com.intive.patronage.toz.pet;

import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.pet.model.db.Pet;
import com.intive.patronage.toz.status.PetsStatusRepository;
import com.intive.patronage.toz.status.model.PetStatus;
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
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

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

    @DataProvider
    public static Object[] getPetWithoutStatus() {
        Pet pet = new Pet();
        pet.setName(EXPECTED_NAME);
        pet.setType(EXPECTED_TYPE);
        pet.setSex(EXPECTED_SEX);
        pet.setPetStatus(null);
        return new Pet[]{pet};
    }

    @DataProvider
    public static Object[] getPetWithStatus() {
        PetStatus petStatus = new PetStatus();
        Pet pet = (Pet) getPetWithoutStatus()[0];
        pet.setPetStatus(petStatus);
        return new Pet[]{pet};
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        petsService = new PetsService(petsRepository, petsStatusRepository);
    }

    @Test
    public void findAllPets() throws Exception {
        when(petsRepository.findAll()).thenReturn(Collections.emptyList());
        List<Pet> pets = petsService.findAllPets();
        assertTrue(pets.isEmpty());
    }

    @Test
    public void findAllPetsWithFilledFields() { // TODO: check information for 'else' in TODO in this method
        when(petsRepository.findByNameNotNullAndTypeNotNullAndSexNotNull()).thenReturn(Collections.emptyList());
        List<Pet> pets = petsService.findPetsWithFilledFields();
        assertTrue(pets.isEmpty());
    }

    @Test
    @UseDataProvider("getPetWithoutStatus")
    public void findById(final Pet pet) throws Exception {
        final UUID petId = pet.getId();
        when(petsRepository.exists(petId)).thenReturn(true);
        when(petsRepository.findOne(petId)).thenReturn(pet);

        Pet dbPet = petsService.findById(petId);
        assertEquals(EXPECTED_NAME, dbPet.getName());
        assertEquals(EXPECTED_TYPE, dbPet.getType());
        assertEquals(EXPECTED_SEX, dbPet.getSex());
    }

    @Test(expected = NotFoundException.class)
    public void findByIdNotFoundException() throws Exception {
        final UUID id = UUID.randomUUID();
        when(petsRepository.exists(id)).thenReturn(false);
        petsService.findById(id);
    }

    @Test
    @UseDataProvider("getPetWithoutStatus")
    public void createPetWithoutStatus(final Pet pet) throws Exception {
        when(petsRepository.save(pet)).thenReturn(pet);

        Pet createdPet = petsService.createPet(pet);
        assertEquals(EXPECTED_NAME, createdPet.getName());
        assertEquals(EXPECTED_TYPE, createdPet.getType());
        assertEquals(EXPECTED_SEX, createdPet.getSex());
    }

    @Test
    @UseDataProvider("getPetWithStatus")
    public void createPetWithStatus(final Pet pet) {
        PetStatus petStatus = pet.getPetStatus();
        UUID petStatusId = petStatus.getId();
        when(petsStatusRepository.exists(petStatusId)).thenReturn(true);
        when(petsStatusRepository.findOne(petStatusId)).thenReturn(petStatus);
        when(petsRepository.save(pet)).thenReturn(pet);

        Pet createdPet = petsService.createPet(pet);
        assertEquals(petStatus, createdPet.getPetStatus());
    }

    @Test
    public void deletePet() throws Exception {
        final UUID petId = UUID.randomUUID();
        when(petsRepository.exists(petId)).thenReturn(true);
        petsService.deletePet(petId);
    }

    @Test(expected = NotFoundException.class)
    public void deletePetNotFoundException() throws Exception {
        final UUID petId = UUID.randomUUID();
        when(petsRepository.exists(petId)).thenReturn(false);
        petsService.deletePet(petId);
    }

    @Test
    @UseDataProvider("getPetWithoutStatus")
    public void updatePet(final Pet pet) throws Exception {
        final UUID petId = pet.getId();
        when(petsRepository.exists(petId)).thenReturn(true);
        when(petsRepository.findOne(petId)).thenReturn(pet);
        when(petsRepository.save(any(Pet.class))).thenReturn(pet);
        Pet savedPet = petsService.updatePet(petId, pet);

        assertEquals(EXPECTED_NAME, savedPet.getName());
        assertEquals(EXPECTED_TYPE, savedPet.getType());
        assertEquals(EXPECTED_SEX, savedPet.getSex());
    }

    @Test(expected = NotFoundException.class)
    @UseDataProvider("getPetWithStatus")
    public void updatePetWithNonExistingStatusNotFoundException(final Pet pet) {
        UUID petId = pet.getId();
        when(petsRepository.exists(petId)).thenReturn(true);
        when(petsRepository.findOne(petId)).thenReturn(pet);
        when(petsStatusRepository.exists(pet.getPetStatus().getId())).thenReturn(false);
        petsService.updatePet(petId, pet);
    }

    @Test
    @UseDataProvider("getPetWithStatus")
    public void updatePetWithStatus(final Pet pet) {
        UUID petId = pet.getId();
        when(petsRepository.exists(petId)).thenReturn(true);
        when(petsRepository.findOne(petId)).thenReturn(pet);
        when(petsStatusRepository.exists(pet.getPetStatus().getId())).thenReturn(true);
        when(petsRepository.save(pet)).thenReturn(pet);
        Pet savedPet = petsService.updatePet(petId, pet);
        assertEquals(savedPet, pet);
    }

    @Test(expected = NotFoundException.class)
    @UseDataProvider("getPetWithoutStatus")
    public void updatePetNotFoundException(final Pet pet) throws Exception {
        final UUID petId = pet.getId();
        when(petsRepository.exists(petId)).thenReturn(false);
        petsService.updatePet(petId, pet);
    }

}
