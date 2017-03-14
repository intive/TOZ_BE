package com.intive.patronage.toz.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intive.patronage.toz.model.constant.PetsConstants;
import com.intive.patronage.toz.model.db.Pet;
import com.intive.patronage.toz.service.PetsService;
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

    private static final  int PETS_LIST_SIZE = 5;
    private static final String EXPECTED_NAME = "Johny";
    private static final Pet.Sex EXPECTED_SEX = Pet.Sex.MALE;
    private static final Pet.Type EXPECTED_TYPE = Pet.Type.DOG;
    private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;
    private final List<Pet> pets = new ArrayList<>();

    @Mock
    private PetsService petsService;
    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(new PetsController(petsService)).build();

        for (int i = 0; i < PETS_LIST_SIZE; i++) {
            Pet pet = new Pet();
            pet.setName("Name:" + i);
            pet.setType(Pet.Type.values()[i%2]);
            pet.setSex(Pet.Sex.values()[i%2]);
            pets.add(pet);
        }
    }

    @DataProvider
    public static Object[] getProperPet() {
        Pet pet = new Pet();
        pet.setName(EXPECTED_NAME);
        pet.setSex(EXPECTED_SEX);
        pet.setType(EXPECTED_TYPE);
        return new Pet[]{pet};
    }

    @Test
    public void getAllPetsOk() throws Exception {
        when(petsService.findAllPets()).thenReturn(pets);

        mvc.perform(get(PetsConstants.PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(PETS_LIST_SIZE)));

        verify(petsService, times(1)).findAllPets();
        verifyNoMoreInteractions(petsService);
    }

    @Test
    @UseDataProvider("getProperPet")
    public void getPetByIdOk(final Pet pet) throws Exception {
        UUID expectedId = pet.getId();

        when(petsService.findById(expectedId)).thenReturn(pet);
        mvc.perform(get(PetsConstants.PATH + "/" + expectedId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$.id", is(expectedId.toString())))
                .andExpect(jsonPath("$.name", is(EXPECTED_NAME)))
                .andExpect(jsonPath("$.sex", is(EXPECTED_SEX.toString())))
                .andExpect(jsonPath("$.type", is(EXPECTED_TYPE.toString())));

        verify(petsService, times(1)).findById(expectedId);
        verifyNoMoreInteractions(petsService);
    }

    @Test
    @UseDataProvider("getProperPet")
    public void createPetOk(final Pet pet) throws Exception {
        String petJsonString = convertToJsonString(pet);

        when(petsService.createPet(any(Pet.class))).thenReturn(pet);
        mvc.perform(post(PetsConstants.PATH)
                .contentType(CONTENT_TYPE)
                .content(petJsonString))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(EXPECTED_NAME)))
                .andExpect(jsonPath("$.sex", is(EXPECTED_SEX.toString())))
                .andExpect(jsonPath("$.type", is(EXPECTED_TYPE.toString())));

        verify(petsService, times(1)).createPet(any(Pet.class));
        verifyNoMoreInteractions(petsService);
    }

    @Test
    public void deletePetById() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(petsService).deletePet(id);
        mvc.perform(delete(PetsConstants.PATH + "/" + id))
                .andExpect(status().isOk());

        verify(petsService, times(1)).deletePet(id);
        verifyNoMoreInteractions(petsService);
    }

    @Test
    @UseDataProvider("getProperPet")
    public void updatePet(final Pet pet) throws Exception {
        String petJsonString = convertToJsonString(pet);
        UUID id = pet.getId();

        when(petsService.updatePet(eq(id), any(Pet.class))).thenReturn(pet);
        mvc.perform(put(PetsConstants.PATH + "/" + id)
                .contentType(CONTENT_TYPE)
                .content(petJsonString))
                .andExpect(status().isOk());

        verify(petsService, times(1)).updatePet(eq(id), any(Pet.class));
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
