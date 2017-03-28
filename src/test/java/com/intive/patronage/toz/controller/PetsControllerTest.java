package com.intive.patronage.toz.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intive.patronage.toz.model.constant.ApiUrl;
import com.intive.patronage.toz.model.db.Pet;
import com.intive.patronage.toz.model.view.PetView;
import com.intive.patronage.toz.service.PetsService;
import com.intive.patronage.toz.service.StorageProperties;
import com.intive.patronage.toz.service.StorageService;
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

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(DataProviderRunner.class)
public class PetsControllerTest {

    private static final int PETS_LIST_SIZE = 5;
    private static final UUID EXPECTED_ID = UUID.randomUUID();
    private static final String EXPECTED_NAME = "Johny";
    private static final String EXPECTED_SEX_VALUE = Pet.Sex.MALE.toString();
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
        mvc = MockMvcBuilders.standaloneSetup(new PetsController(petsService, storageService, storageProperties)).build();
    }

    @DataProvider
    public static Object[] getProperPet() {
        PetView pet = new PetView();
        pet.setId(EXPECTED_ID);
        pet.setName(EXPECTED_NAME);
        pet.setSex(EXPECTED_SEX_VALUE);
        pet.setType(EXPECTED_TYPE_VALUE);
        return new PetView[]{pet};
    }

    @Test
    public void getAllPetsOk() throws Exception {
        final List<PetView> pets = getPets();
        when(petsService.findAllPets()).thenReturn(pets);

        mvc.perform(get(ApiUrl.PET_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(PETS_LIST_SIZE)));

        verify(petsService, times(1)).findAllPets();
        verifyNoMoreInteractions(petsService);
    }

    private List<PetView> getPets() {
        final List<PetView> pets = new ArrayList<>();
        for (int i = 0; i < PETS_LIST_SIZE; i++) {
            PetView pet = new PetView();
            pet.setName("Name:" + i);
            pet.setType(Pet.Type.values()[i%2].toString());
            pet.setSex(Pet.Sex.values()[i%2].toString());
            pets.add(pet);
        }
        return pets;
    }

    @Test
    @UseDataProvider("getProperPet")
    public void getPetByIdOk(final PetView pet) throws Exception {
        when(petsService.findById(EXPECTED_ID)).thenReturn(pet);
        mvc.perform(get(ApiUrl.PET_PATH + "/" + EXPECTED_ID))
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
    public void createPetOk(final PetView pet) throws Exception {
        String petJsonString = convertToJsonString(pet);

        when(petsService.createPet(any(PetView.class))).thenReturn(pet);
        mvc.perform(post(ApiUrl.PET_PATH)
                .contentType(CONTENT_TYPE)
                .content(petJsonString))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(EXPECTED_NAME)))
                .andExpect(jsonPath("$.sex", is(EXPECTED_SEX_VALUE)))
                .andExpect(jsonPath("$.type", is(EXPECTED_TYPE_VALUE)));

        verify(petsService, times(1)).createPet(any(PetView.class));
        verifyNoMoreInteractions(petsService);
    }

    @Test
    public void deletePetById() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(petsService).deletePet(id);
        mvc.perform(delete(ApiUrl.PET_PATH + "/" + id))
                .andExpect(status().isOk());

        verify(petsService, times(1)).deletePet(id);
        verifyNoMoreInteractions(petsService);
    }

    @Test
    @UseDataProvider("getProperPet")
    public void updatePet(final PetView pet) throws Exception {
        String petJsonString = convertToJsonString(pet);

        when(petsService.updatePet(eq(EXPECTED_ID), any(PetView.class))).thenReturn(pet);
        mvc.perform(put(ApiUrl.PET_PATH + "/" + EXPECTED_ID)
                .contentType(CONTENT_TYPE)
                .content(petJsonString))
                .andExpect(status().isOk());

        verify(petsService, times(1)).updatePet(eq(EXPECTED_ID), any(PetView.class));
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
