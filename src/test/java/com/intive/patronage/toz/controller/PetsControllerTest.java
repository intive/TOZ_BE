package com.intive.patronage.toz.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intive.patronage.toz.model.db.Pet;
import com.intive.patronage.toz.service.PetsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
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

@RunWith(SpringRunner.class)
public class PetsControllerTest {

    private final static String PATH = "/pets";
    private final static int PETS_LIST_SIZE = 5;
    private final static MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;
    private final List<Pet> pets = new ArrayList<>();

    @Mock
    private PetsService petsService;
    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(new PetsController(petsService)).build();

        for (int i = 0; i < PETS_LIST_SIZE; i++) {
            Pet pet = new Pet();
            pet.setName("Name:" + i);
            pet.setType(Pet.Type.values()[i%2]);
            pet.setSex(Pet.Sex.values()[i%2]);
            pets.add(pet);
        }
    }

    @Test
    public void getAllPetsOk() throws Exception {
        when(petsService.findAllPets()).thenReturn(pets);

        mvc.perform(get(PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(PETS_LIST_SIZE)));

        verify(petsService, times(1)).findAllPets();
        verifyNoMoreInteractions(petsService);
    }

    @Test
    public void getPetByIdOk() throws Exception {
        String expectedName = "Johny";
        Pet pet = new Pet();
        pet.setName(expectedName);
        pet.setSex(Pet.Sex.MALE);
        pet.setType(Pet.Type.DOG);
        UUID expectedId = pet.getId();

        when(petsService.findById(expectedId)).thenReturn(pet);
        mvc.perform(get(PATH + "/" + expectedId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$.id", is(expectedId.toString())))
                .andExpect(jsonPath("$.name", is(expectedName)))
                .andExpect(jsonPath("$.sex", is(Pet.Sex.MALE.toString())))
                .andExpect(jsonPath("$.type", is(Pet.Type.DOG.toString())));

        verify(petsService, times(1)).findById(expectedId);
        verifyNoMoreInteractions(petsService);
    }

    @Test
    public void createPetOk() throws Exception {
        String expectedName = "Johny";
        Pet pet = new Pet();
        pet.setName(expectedName);
        pet.setSex(Pet.Sex.MALE);
        pet.setType(Pet.Type.DOG);
        String petJsonString = convertToJsonString(pet);

        when(petsService.createPet(any(Pet.class))).thenReturn(pet);
        mvc.perform(post(PATH)
                .contentType(CONTENT_TYPE)
                .content(petJsonString))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(expectedName)))
                .andExpect(jsonPath("$.sex", is(Pet.Sex.MALE.toString())))
                .andExpect(jsonPath("$.type", is(Pet.Type.DOG.toString())));

        verify(petsService, times(1)).createPet(any(Pet.class));
        verifyNoMoreInteractions(petsService);
    }

    @Test
    public void deletePetById() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(petsService).deletePet(id);
        mvc.perform(delete(PATH + "/" + id))
                .andExpect(status().isOk());

        verify(petsService, times(1)).deletePet(id);
        verifyNoMoreInteractions(petsService);
    }

    @Test
    public void updatePet() throws Exception {
        Pet pet = new Pet();
        pet.setName("Johny");
        pet.setSex(Pet.Sex.MALE);
        pet.setType(Pet.Type.DOG);
        String petJsonString = convertToJsonString(pet);
        UUID id = pet.getId();

        when(petsService.updatePet(eq(id), any(Pet.class))).thenReturn(pet);
        mvc.perform(put(PATH + "/" + id)
                .contentType(CONTENT_TYPE)
                .content(petJsonString))
                .andExpect(status().isOk());

        verify(petsService, times(1)).updatePet(eq(id), any(Pet.class));
        verifyNoMoreInteractions(petsService);
    }

    public static String convertToJsonString(Object value) {
        try {
            return new ObjectMapper().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}