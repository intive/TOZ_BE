package com.intive.patronage.toz.pet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.pet.model.db.Pet;
import com.intive.patronage.toz.storage.StorageProperties;
import com.intive.patronage.toz.storage.StorageService;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(DataProviderRunner.class)
public class PetsControllerTest {

    private static final String ADMIN = "admin";
    private static final int PETS_LIST_SIZE = 5;
    private static final UUID EXPECTED_ID = UUID.randomUUID();
    private static final String EXPECTED_NAME = "Johny";
    private static final Pet.Sex EXPECTED_SEX = Pet.Sex.MALE;
    private static final String EXPECTED_SEX_VALUE = Pet.Sex.MALE.toString();
    private static final Pet.Type EXPECTED_TYPE = Pet.Type.DOG;
    private static final String EXPECTED_TYPE_VALUE = Pet.Type.DOG.toString();
    private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;

    @Mock
    private PetsService petsService;
    @Mock
    private StorageService storageService;
    @Mock
    private StorageProperties storageProperties;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        final PetsController petsController = new PetsController(petsService, storageService, storageProperties);
        mvc = MockMvcBuilders.standaloneSetup(petsController).build();
    }

    @DataProvider
    public static Object[] getProperPet() {
        Pet pet = new Pet();
        pet.setId(EXPECTED_ID);
        pet.setName(EXPECTED_NAME);
        pet.setSex(EXPECTED_SEX);
        pet.setType(EXPECTED_TYPE);
        return new Pet[]{pet};
    }

    @Test
    public void getAllPetsOk() throws Exception {
        final List<Pet> pets = getPets();
        when(petsService.findAllPets()).thenReturn(pets);

        mvc.perform(get(ApiUrl.PETS_PATH).param(ADMIN, Boolean.TRUE.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(PETS_LIST_SIZE)));

        verify(petsService, times(1)).findAllPets();
        verifyNoMoreInteractions(petsService);
    }

    private List<Pet> getPets() {
        final List<Pet> pets = new ArrayList<>();
        for (int i = 0; i < PETS_LIST_SIZE; i++) {
            Pet pet = new Pet();
            pet.setName("Name:" + i);
            pet.setType(Pet.Type.values()[i%2]);
            pet.setSex(Pet.Sex.values()[i%2]);
            pets.add(pet);
        }
        return pets;
    }

    @Test
    @UseDataProvider("getProperPet")
    public void getPetByIdOk(final Pet pet) throws Exception {
        when(petsService.findById(EXPECTED_ID)).thenReturn(pet);
        mvc.perform(get(ApiUrl.PETS_PATH + "/" + EXPECTED_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$.id", is(EXPECTED_ID.toString())))
                .andExpect(jsonPath("$.name", is(EXPECTED_NAME)))
                .andExpect(jsonPath("$.sex", is(EXPECTED_SEX_VALUE)))
                .andExpect(jsonPath("$.type", is(EXPECTED_TYPE_VALUE)));

        verify(petsService, times(1)).findById(EXPECTED_ID);
        verifyNoMoreInteractions(petsService);
    }

    @Test
    @UseDataProvider("getProperPet")
    public void createPetOk(final Pet pet) throws Exception {
        String petJsonString = convertToJsonString(pet);

        when(petsService.createPet(any(Pet.class))).thenReturn(pet);
        mvc.perform(post(ApiUrl.PETS_PATH)
                .contentType(CONTENT_TYPE)
                .content(petJsonString))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(EXPECTED_NAME)))
                .andExpect(jsonPath("$.sex", is(EXPECTED_SEX_VALUE)))
                .andExpect(jsonPath("$.type", is(EXPECTED_TYPE_VALUE)));

        verify(petsService, times(1)).createPet(any(Pet.class));
        verifyNoMoreInteractions(petsService);
    }

    @Test
    public void deletePetById() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(petsService).deletePet(id);
        mvc.perform(delete(ApiUrl.PETS_PATH + "/" + id))
                .andExpect(status().isOk());

        verify(petsService, times(1)).deletePet(id);
        verifyNoMoreInteractions(petsService);
    }

    @Test
    @UseDataProvider("getProperPet")
    public void updatePet(final Pet pet) throws Exception {
        String petJsonString = convertToJsonString(pet);

        when(petsService.updatePet(eq(EXPECTED_ID), any(Pet.class))).thenReturn(pet);
        mvc.perform(put(ApiUrl.PETS_PATH + "/" + EXPECTED_ID)
                .contentType(CONTENT_TYPE)
                .content(petJsonString))
                .andExpect(status().isOk());

        verify(petsService, times(1)).updatePet(eq(EXPECTED_ID), any(Pet.class));
        verifyNoMoreInteractions(petsService);
    }

    private static String convertToJsonString(Object value) {
        try {
            return new ObjectMapper().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
