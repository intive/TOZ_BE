package com.intive.patronage.toz.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intive.patronage.toz.model.constant.PetValues;
import com.intive.patronage.toz.model.view.PetView;
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
    private static final PetValues.Sex EXPECTED_SEX = PetValues.Sex.MALE;
    private static final PetValues.Type EXPECTED_TYPE = PetValues.Type.DOG;
    private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;
    private final List<PetView> pets = new ArrayList<>();

    @Mock
    private PetsService petsService;
    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(new PetsController(petsService)).build();

        for (int i = 0; i < PETS_LIST_SIZE; i++) {
            PetView pet = new PetView();
            pet.setName("Name:" + i);
            pet.setType(PetValues.Type.values()[i%2]);
            pet.setSex(PetValues.Sex.values()[i%2]);
            pets.add(pet);
        }
    }

    @DataProvider
    public static Object[] getProperPet() {
        PetView pet = new PetView();
        pet.setName(EXPECTED_NAME);
        pet.setSex(EXPECTED_SEX);
        pet.setType(EXPECTED_TYPE);
        return new PetView[]{pet};
    }

    @Test
    public void getAllPetsOk() throws Exception {
        when(petsService.findAllPets()).thenReturn(pets);

        mvc.perform(get(PetValues.PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(PETS_LIST_SIZE)));

        verify(petsService, times(1)).findAllPets();
        verifyNoMoreInteractions(petsService);
    }

    @Test
    @UseDataProvider("getProperPet")
    public void getPetByIdOk(final PetView pet) throws Exception {
        UUID expectedId = pet.getId();

        when(petsService.findById(expectedId)).thenReturn(pet);
        mvc.perform(get(PetValues.PATH + "/" + expectedId))
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
    public void createPetOk(final PetView pet) throws Exception {
        String petJsonString = convertToJsonString(pet);

        when(petsService.createPet(any(PetView.class))).thenReturn(pet);
        mvc.perform(post(PetValues.PATH)
                .contentType(CONTENT_TYPE)
                .content(petJsonString))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(EXPECTED_NAME)))
                .andExpect(jsonPath("$.sex", is(EXPECTED_SEX.toString())))
                .andExpect(jsonPath("$.type", is(EXPECTED_TYPE.toString())));

        verify(petsService, times(1)).createPet(any(PetView.class));
        verifyNoMoreInteractions(petsService);
    }

    @Test
    public void deletePetById() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(petsService).deletePet(id);
        mvc.perform(delete(PetValues.PATH + "/" + id))
                .andExpect(status().isOk());

        verify(petsService, times(1)).deletePet(id);
        verifyNoMoreInteractions(petsService);
    }

    @Test
    @UseDataProvider("getProperPet")
    public void updatePet(final PetView pet) throws Exception {
        String petJsonString = convertToJsonString(pet);
        final UUID id = pet.getId();

        when(petsService.updatePet(eq(id), any(PetView.class))).thenReturn(pet);
        mvc.perform(put(PetValues.PATH + "/" + id)
                .contentType(CONTENT_TYPE)
                .content(petJsonString))
                .andExpect(status().isOk());

        verify(petsService, times(1)).updatePet(eq(id), any(PetView.class));
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
