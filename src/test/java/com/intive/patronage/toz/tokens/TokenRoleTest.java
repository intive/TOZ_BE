package com.intive.patronage.toz.tokens;

import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.environment.ApiProperties;
import com.intive.patronage.toz.users.UserService;
import com.intive.patronage.toz.users.model.db.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.intive.patronage.toz.tokens.TokenControllerTest.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {ApiProperties.JWT_SECRET_BASE64, ApiProperties.SUPER_ADMIN_PASSWORD}
)
@ActiveProfiles("test")
public class TokenRoleTest {

    private static final String TYPE_PARAM = "type";
    private static final String RELEASED = "RELEASED";
    private static final String ACHIEVED = "ACHIEVED";
    private static final String EMAIL_VOLUNTEER = "volunteer@mail.com";
    private static final User.Role ROLE_VOLUNTEER = User.Role.VOLUNTEER;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtFactory jwtFactory;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private User admin;
    private User volunteer;

    @Before
    public void setUp() throws Exception {
        final String passwordHash = passwordEncoder.encode(PASSWORD);
        admin = new User();
        volunteer = new User();
        admin.addRole(ROLE_ADMIN);
        volunteer.addRole(ROLE_VOLUNTEER);
        admin.setEmail(EMAIL);
        volunteer.setEmail(EMAIL_VOLUNTEER);
        admin.setPasswordHash(passwordHash);
        volunteer.setPasswordHash(passwordHash);
        userService.createWithPasswordHash(admin, passwordHash);
        userService.createWithPasswordHash(volunteer, passwordHash);
    }

    @After
    public void tearDown() throws Exception {
        userService.delete(admin.getId());
        userService.delete(volunteer.getId());
    }

    @Test
    public void shouldReturnOkWhenAdminAndGetNewsReleased() throws Exception {
        mockMvc.perform(get(ApiUrl.NEWS_PATH).param(TYPE_PARAM, RELEASED)
                .header(AUTHORIZATION_HEADER, TOKEN_PREFIX + jwtFactory.generateToken(admin)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnForbiddenWhenVolunteerAndNewsAchieved() throws Exception {
        mockMvc.perform(get(ApiUrl.NEWS_PATH).param(TYPE_PARAM, ACHIEVED)
                .header(AUTHORIZATION_HEADER, TOKEN_PREFIX + jwtFactory.generateToken(volunteer)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void shouldReturnOkWhenVolunteerAndNewsReleased() throws Exception {
        mockMvc.perform(get(ApiUrl.NEWS_PATH).param(TYPE_PARAM, RELEASED)
                .header(AUTHORIZATION_HEADER, TOKEN_PREFIX + jwtFactory.generateToken(volunteer)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnOkWhenAnonymousAndGetNewsReleased() throws Exception {
        mockMvc.perform(get(ApiUrl.NEWS_PATH).param(TYPE_PARAM, RELEASED))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnForbiddenWhenAnonymousAndNewsAchieved() throws Exception {
        mockMvc.perform(get(ApiUrl.NEWS_PATH).param(TYPE_PARAM, ACHIEVED))
                .andExpect(status().isForbidden());
    }
}
