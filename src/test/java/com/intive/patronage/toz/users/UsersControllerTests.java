package com.intive.patronage.toz.users;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intive.patronage.toz.users.model.db.User;
import com.intive.patronage.toz.users.model.enumerations.Role;
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
public class UsersControllerTests {

    private static final String URL_PATH = "/users";
    private static final int PETS_LIST_SIZE = 5;
    private static final UUID EXPECTED_ID = UUID.randomUUID();
    private static final String EXPECTED_USERNAME = "johny";
    private static final String EXPECTED_PASSWORD = "";
    private static final String EXPECTED_SURNAME = "Johny";
    private static final String EXPECTED_FORENAME = "Cage";
    private static final Role EXPECTED_ROLE = Role.TOZ;
    private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;

    @Mock
    private UserService userService;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mvc = MockMvcBuilders.standaloneSetup(new UsersController(userService)).build();

    }

    @DataProvider
    public static Object[] getProperUser() {
        User user = new User();
        user.setId(EXPECTED_ID);
        user.setName(EXPECTED_USERNAME);
        user.setPassword(EXPECTED_PASSWORD);
        user.setSurname(EXPECTED_SURNAME);
        user.setRole(EXPECTED_ROLE);
        return new User[]{user};
    }

    private List<User> getUsers() {
        final List<User> users = new ArrayList<>();
        for (int i = 0; i < PETS_LIST_SIZE; i++) {
            User user = new User();
            user.setId(UUID.randomUUID());
            user.setName(String.format("%s_%d", "user", i));
            user.setPassword(String.format("%s_%d", "password", i));
            user.setSurname(String.format("%s_%d", "Surname", i));
            user.setRole(Role.values()[i % 2]);
            users.add(user);
        }
        return users;
    }

    private static String convertToJsonString(Object value) {
        try {
            return new ObjectMapper().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getAllUsersOk() throws Exception {
        final List<User> users = getUsers();
        when(userService.findAll()).thenReturn(users);

        mvc.perform(get(URL_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(PETS_LIST_SIZE)));

        verify(userService, times(1)).findAll();
        verifyNoMoreInteractions(userService);
    }

    @Test
    @UseDataProvider("getProperUser")
    public void createUserOk(final User user) throws Exception {
        String userJsonString = convertToJsonString(user);

        when(userService.create(any(User.class))).thenReturn(user);
        mvc.perform(post(URL_PATH)
                .contentType(CONTENT_TYPE)
                .content(userJsonString))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is(EXPECTED_USERNAME)))
                .andExpect(jsonPath("$.password", is(EXPECTED_PASSWORD)))
                .andExpect(jsonPath("$.forename", is(EXPECTED_FORENAME)))
                .andExpect(jsonPath("$.surname", is(EXPECTED_SURNAME)))
                .andExpect(jsonPath("$.role", is(EXPECTED_ROLE.toString())));

        verify(userService, times(1)).create(any(User.class));
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void deleteUserById() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(userService).delete(id);
        mvc.perform(delete(String.format("%s/%s", URL_PATH, id)))
                .andExpect(status().isOk());

        verify(userService, times(1)).delete(id);
        verifyNoMoreInteractions(userService);
    }

    @Test
    @UseDataProvider("getProperUser")
    public void updateUser(final User user) throws Exception {
        String userJsonString = convertToJsonString(user);

        when(userService.update(eq(EXPECTED_ID), any(User.class))).thenReturn(user);
        mvc.perform(put(String.format("%s/%s", URL_PATH, EXPECTED_ID))
                .contentType(CONTENT_TYPE)
                .content(userJsonString))
                .andExpect(status().isOk());

        verify(userService, times(1)).update(eq(EXPECTED_ID), any(User.class));
        verifyNoMoreInteractions(userService);
    }
}
