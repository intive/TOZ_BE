package com.intive.patronage.toz.users;

import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.environment.ApiProperties;
import com.intive.patronage.toz.tokens.JwtFactory;
import com.intive.patronage.toz.users.model.db.Role;
import com.intive.patronage.toz.users.model.db.User;
import com.intive.patronage.toz.users.model.view.UserView;
import com.intive.patronage.toz.util.ModelMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.Matchers.not;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestPropertySource(
        properties = {
                ApiProperties.JWT_SECRET_BASE64,
                ApiProperties.SUPER_ADMIN_PASSWORD
        }
)
@ActiveProfiles("test")
public class UserTozAdminControllerTest {

    private static final long EXPIRATION_TIME = 5;
    private static final String AUTHORIZATION = "Authorization";
    private static final String PASSWORD = "password";
    private static final UUID RANDOM_UUID = UUID.randomUUID();
    private static final MediaType JSON_CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;

    @Value("${jwt.secret-base64}")
    private String secret;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;
    private JwtFactory jwtFactory;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        jwtFactory = new JwtFactory(secret);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void getAllUsersWithTozRoleIsOk() throws Exception {
        final String tozUserAuthorizationToken = getAuthorizationTokenWithRole(Role.TOZ);
        mockMvc.perform(get(ApiUrl.USERS_PATH).header(AUTHORIZATION, tozUserAuthorizationToken))
                .andExpect(status().isOk());
    }

    private String getAuthorizationTokenWithRole(final Role role) {
        final User user = UserTestsUtils.getUserWithRole(role);
        final String token = jwtFactory.generateToken(user, EXPIRATION_TIME);
        return String.format("%s %s", "Bearer", token);
    }

    @Test
    public void getAllUsersWithoutRoleIsForbidden() throws Exception {
        mockMvc.perform(get(ApiUrl.USERS_PATH))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getAllUsersWithVolunteerRoleIsForbidden() throws Exception {
        final String volunteerUserAuthorizationToken = getAuthorizationTokenWithRole(Role.VOLUNTEER);
        mockMvc.perform(get(ApiUrl.USERS_PATH).header(AUTHORIZATION, volunteerUserAuthorizationToken))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getUserByIdWithTozRoleIsNotForbidden() throws Exception {
        final String requestUrl = String.format("%s/%s", ApiUrl.USERS_PATH, RANDOM_UUID);
        final String tozUserAuthorizationToken = getAuthorizationTokenWithRole(Role.TOZ);
        mockMvc.perform(get(requestUrl).header(AUTHORIZATION, tozUserAuthorizationToken))
                .andExpect(status().is(not(HttpStatus.FORBIDDEN.value())));
    }

    @Test
    public void getUserByIdWithVolunteerRoleIsForbidden() throws Exception {
        final String requestUrl = String.format("%s/%s", ApiUrl.USERS_PATH, RANDOM_UUID);
        final String volunteerUserAuthorizationToken = getAuthorizationTokenWithRole(Role.VOLUNTEER);
        mockMvc.perform(get(requestUrl).header(AUTHORIZATION, volunteerUserAuthorizationToken))
                .andExpect(status().isForbidden());
    }

    @Test
    public void getUserByIdWithoutRoleIsForbidden() throws Exception {
        String requestUrl = String.format("%s/%s", ApiUrl.USERS_PATH, RANDOM_UUID);
        mockMvc.perform(get(requestUrl))
                .andExpect(status().isForbidden());
    }

    @Test
    public void createUserWithTozRoleIsNotForbidden() throws Exception {
        final String tozUserAuthorizationToken = getAuthorizationTokenWithRole(Role.TOZ);
        mockMvc.perform(post(ApiUrl.SUPER_ADMIN_USERS_PATH)
                .header(AUTHORIZATION, tozUserAuthorizationToken)
                .content(getUserContentBody())
                .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().is(not(HttpStatus.FORBIDDEN.value())));
    }

    private String getUserContentBody() {
        final User volunteerUser = UserTestsUtils.getUserWithRole(Role.VOLUNTEER);
        final UserView volunteerUserView = ModelMapper.convertToView(volunteerUser, UserView.class);
        volunteerUserView.setPassword(PASSWORD);
        return ModelMapper.convertToJsonString(volunteerUserView);
    }

    @Test
    public void createUserWithVolunteerRoleIsForbidden() throws Exception {
        final String volunteerUserAuthorizationToken = getAuthorizationTokenWithRole(Role.VOLUNTEER);
        mockMvc.perform(post(ApiUrl.USERS_PATH)
                .header(AUTHORIZATION, volunteerUserAuthorizationToken)
                .content(getUserContentBody())
                .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isForbidden());
    }

    @Test
    public void createUserWithoutRoleIsForbidden() throws Exception {
        mockMvc.perform(post(ApiUrl.USERS_PATH)
                .content(getUserContentBody())
                .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteUserWithTozRoleIsNotForbidden() throws Exception {
        final String requestUrl = String.format("%s/%s", ApiUrl.USERS_PATH, RANDOM_UUID);
        final String tozUserAuthorizationToken = getAuthorizationTokenWithRole(Role.TOZ);
        mockMvc.perform(delete(requestUrl).header(AUTHORIZATION, tozUserAuthorizationToken))
                .andExpect(status().is(not(HttpStatus.FORBIDDEN.value())));
    }

    @Test
    public void deleteUserWithVolunteerRoleIsForbidden() throws Exception {
        final String requestUrl = String.format("%s/%s", ApiUrl.USERS_PATH, RANDOM_UUID);
        final String volunteerUserAuthorizationToken = getAuthorizationTokenWithRole(Role.VOLUNTEER);
        mockMvc.perform(delete(requestUrl).header(AUTHORIZATION, volunteerUserAuthorizationToken))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteUserWithoutRoleIsForbidden() throws Exception {
        final String requestUrl = String.format("%s/%s", ApiUrl.USERS_PATH, RANDOM_UUID);
        mockMvc.perform(delete(requestUrl))
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateUserWithTozRoleIsNotForbidden() throws Exception {
        final String requestUrl = String.format("%s/%s", ApiUrl.USERS_PATH, RANDOM_UUID);
        final String tozUserAuthorizationToken = getAuthorizationTokenWithRole(Role.TOZ);
        mockMvc.perform(put(requestUrl)
                .header(AUTHORIZATION, tozUserAuthorizationToken)
                .content(getUserContentBody())
                .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().is(not(HttpStatus.FORBIDDEN.value())));
    }

    @Test
    public void updateUserWithVolunteerRoleIsForbidden() throws Exception {
        final String requestUrl = String.format("%s/%s", ApiUrl.USERS_PATH, RANDOM_UUID);
        final String volunteerUserAuthorizationToken = getAuthorizationTokenWithRole(Role.VOLUNTEER);
        mockMvc.perform(get(requestUrl)
                .header(AUTHORIZATION, volunteerUserAuthorizationToken)
                .content(getUserContentBody())
                .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateUserWithoutRoleIsForbidden() throws Exception {
        final String requestUrl = String.format("%s/%s", ApiUrl.USERS_PATH, RANDOM_UUID);
        mockMvc.perform(get(requestUrl)
                .content(getUserContentBody())
                .contentType(JSON_CONTENT_TYPE))
                .andExpect(status().isForbidden());
    }
}
