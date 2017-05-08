package com.intive.patronage.toz.users;

import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.environment.ApiProperties;
import com.intive.patronage.toz.users.model.db.User;
import com.intive.patronage.toz.users.model.view.UserView;
import com.intive.patronage.toz.util.ModelMapper;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.intive.patronage.toz.users.UserDataProvider.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(DataProviderRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = ApiProperties.JWT_SECRET_BASE64
)
public class UserSuperAdminControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        UserSuperAdminController userSuperAdminController = new UserSuperAdminController(userService, passwordEncoder);
        mvc = MockMvcBuilders.standaloneSetup(userSuperAdminController).build();
    }

    private List<User> getUsers() {
        final List<User> users = new ArrayList<>();
        for (int i = 0; i < USERS_LIST_SIZE; i++) {
            final User user = new User();
            user.setId(UUID.randomUUID());
            user.setName(String.format("%s_%d", "name", i));
            user.setPasswordHash(String.format("%s_%d", "password_hash", i));
            user.setSurname(String.format("%s_%d", "surname", i));
            user.setPhoneNumber(String.format("%s_%d", "phone number", i));
            user.setEmail(String.format("%s_%d", "email", i));
            user.addRole(User.Role.values()[i % 2]);
            users.add(user);
        }
        return users;
    }

    @Test
    public void getAllUsersOk() throws Exception {
        final List<User> users = getUsers();
        when(userService.findAll()).thenReturn(users);

        mvc.perform(get(ApiUrl.SUPER_ADMIN_USERS_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(USERS_LIST_SIZE)));

        verify(userService, times(1)).findAll();
        verifyNoMoreInteractions(userService);
    }

    @Test
    @UseDataProvider(value = "getTozAdminUserModel", location = UserDataProvider.class)
    public void getUserByIdOk(final User user) throws Exception {
        when(userService.findOneById(EXPECTED_ID)).thenReturn(user);
        String requestUrl = String.format("%s/%s", ApiUrl.SUPER_ADMIN_USERS_PATH, EXPECTED_ID.toString());
        mvc.perform(get(requestUrl))
                .andExpect(status().isOk())
                .andExpect(content().contentType(JSON_CONTENT_TYPE))
                .andExpect(jsonPath("$.id", is(EXPECTED_ID.toString())))
                .andExpect(jsonPath("$.name", is(EXPECTED_NAME)))
                .andExpect(jsonPath("$.surname", is(EXPECTED_SURNAME)))
                .andExpect(jsonPath("$.phoneNumber", is(EXPECTED_PHONE_NUMBER)))
                .andExpect(jsonPath("$.email", is(EXPECTED_EMAIL)))
                .andExpect(jsonPath("$.roles[0]", is(TOZ_ROLE.toString())));

        verify(userService, times(1)).findOneById(EXPECTED_ID);
        verifyNoMoreInteractions(userService);
    }

    @Test
    @UseDataProvider(value = "getUserWithView", location = UserDataProvider.class)
    public void createUserOk(final User user, final UserView userView) throws Exception {
        final String userViewJsonString = ModelMapper.convertToJsonString(userView);

        when(userService.createWithPasswordHash(any(User.class), any(String.class))).thenReturn(user);
        mvc.perform(post(ApiUrl.SUPER_ADMIN_USERS_PATH)
                .contentType(JSON_CONTENT_TYPE)
                .content(userViewJsonString))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(EXPECTED_NAME)))
                .andExpect(jsonPath("$.surname", is(EXPECTED_SURNAME)))
                .andExpect(jsonPath("$.phoneNumber", is(EXPECTED_PHONE_NUMBER)))
                .andExpect(jsonPath("$.email", is(EXPECTED_EMAIL)))
                .andExpect(jsonPath("$.roles[0]", is(TOZ_ROLE.toString())));

        verify(userService, times(1))
                .createWithPasswordHash(any(User.class), any(String.class));
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void deleteUserById() throws Exception {
        doNothing().when(userService).delete(EXPECTED_ID);
        String requestUrl = String.format("%s/%s", ApiUrl.SUPER_ADMIN_USERS_PATH, EXPECTED_ID);
        mvc.perform(delete(requestUrl))
                .andExpect(status().isOk());

        verify(userService, times(1)).delete(EXPECTED_ID);
        verifyNoMoreInteractions(userService);
    }

    @Test
    @UseDataProvider(value = "getUserWithView", location = UserDataProvider.class)
    public void updateUser(final User user, final UserView userView) throws Exception {
        final String userJsonString = ModelMapper.convertToJsonString(userView);

        when(userService.update(eq(EXPECTED_ID), any(User.class))).thenReturn(user);
        mvc.perform(put(String.format("%s/%s", ApiUrl.SUPER_ADMIN_USERS_PATH, EXPECTED_ID))
                .contentType(JSON_CONTENT_TYPE)
                .content(userJsonString))
                .andExpect(status().isOk());

        verify(userService, times(1)).update(eq(EXPECTED_ID), any(User.class));
        verifyNoMoreInteractions(userService);
    }
}
