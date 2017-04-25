package com.intive.patronage.toz.users;

import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.users.model.db.User;
import com.intive.patronage.toz.users.model.view.UserView;
import com.intive.patronage.toz.util.ModelMapper;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "jwt.secret-base64=c2VjcmV0"
)
public class UserControllerTest {

    private static final int USERS_LIST_SIZE = 5;
    private static final UUID EXPECTED_ID = UUID.randomUUID();
    private static final String EXPECTED_NAME = "Johny";
    private static final String EXPECTED_PASSWORD = "johnyPassword";
    private static final String EXPECTED_PASSWORD_HASH = "7sdf7sd6f7sd76f";
    private static final String EXPECTED_SURNAME = "Bravo";
    private static final String EXPECTED_PHONE_NUMBER = "111222333";
    private static final String EXPECTED_EMAIL = "johny.bravo@gmail.com";
    private static final User.Role EXPECTED_ROLE = User.Role.TOZ;
    private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        final UserController userController = new UserController(userService, passwordEncoder);
        mvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @DataProvider
    public static Object[][] getUserWithView() {
        final User user = new User();
        user.setId(EXPECTED_ID);
        user.setName(EXPECTED_NAME);
        user.setPasswordHash(EXPECTED_PASSWORD_HASH);
        user.setSurname(EXPECTED_SURNAME);
        user.setPhoneNumber(EXPECTED_PHONE_NUMBER);
        user.setEmail(EXPECTED_EMAIL);
        user.addRole(EXPECTED_ROLE);

        final UserView userView = ModelMapper.convertToView(user, UserView.class);
        userView.setPassword(EXPECTED_PASSWORD);
        return new Object[][]{{user, userView}};
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

        mvc.perform(get(ApiUrl.ADMIN_USERS_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("$", hasSize(USERS_LIST_SIZE)));

        verify(userService, times(1)).findAll();
        verifyNoMoreInteractions(userService);
    }

    @Test
    @UseDataProvider("getUserWithView")
    public void createUserOk(final User user, final UserView userView) throws Exception {
        final String userViewJsonString = ModelMapper.convertToJsonString(userView);

        when(userService.createWithPasswordHash(any(User.class), any(String.class))).thenReturn(user);
        mvc.perform(post(ApiUrl.ADMIN_USERS_PATH)
                .contentType(CONTENT_TYPE)
                .content(userViewJsonString))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(EXPECTED_NAME)))
                .andExpect(jsonPath("$.surname", is(EXPECTED_SURNAME)))
                .andExpect(jsonPath("$.phoneNumber", is(EXPECTED_PHONE_NUMBER)))
                .andExpect(jsonPath("$.email", is(EXPECTED_EMAIL)))
                .andExpect(jsonPath("$.roles[0]", is(EXPECTED_ROLE.toString())));

        verify(userService, times(1))
                .createWithPasswordHash(any(User.class), any(String.class));
        verifyNoMoreInteractions(userService);
    }

    @Test
    public void deleteUserById() throws Exception {
        final UUID id = UUID.randomUUID();
        doNothing().when(userService).delete(id);
        mvc.perform(delete(String.format("%s/%s", ApiUrl.ADMIN_USERS_PATH, id)))
                .andExpect(status().isOk());

        verify(userService, times(1)).delete(id);
        verifyNoMoreInteractions(userService);
    }

    @Test
    @UseDataProvider("getUserWithView")
    public void updateUser(final User user, final UserView userView) throws Exception {
        final String userJsonString = ModelMapper.convertToJsonString(userView);

        when(userService.update(eq(EXPECTED_ID), any(User.class))).thenReturn(user);
        mvc.perform(put(String.format("%s/%s", ApiUrl.ADMIN_USERS_PATH, EXPECTED_ID))
                .contentType(CONTENT_TYPE)
                .content(userJsonString))
                .andExpect(status().isOk());

        verify(userService, times(1)).update(eq(EXPECTED_ID), any(User.class));
        verifyNoMoreInteractions(userService);
    }
}
