package com.intive.patronage.toz.passwords;

import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.environment.ApiProperties;
import com.intive.patronage.toz.passwords.model.PasswordChangeRequestView;
import com.intive.patronage.toz.tokens.model.view.UserCredentialsView;
import com.intive.patronage.toz.users.UserService;
import com.intive.patronage.toz.users.model.db.User;
import com.intive.patronage.toz.util.ModelMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.intive.patronage.toz.config.ApiUrl.ACQUIRE_TOKEN_PATH;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(
        properties = {ApiProperties.JWT_SECRET_BASE64, ApiProperties.SUPER_ADMIN_PASSWORD}
)
@ActiveProfiles("test")
public class PasswordsControllerTest {

    private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;
    private static final String EMAIL = "user@mail.com";
    private static final String OLD_PASSWORD = "OldPassword";
    private static final String NEW_PASSWORD = "NewPassword";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final User.Role USER_ROLE = User.Role.VOLUNTEER;
    private static final String JWT_JSON_SIGN = "\"jwt\":\"";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MessageSource messageSource;

    private User user;
    private String token;

    @Before
    public void setUp() throws Exception {
        final String passwordHash = passwordEncoder.encode(OLD_PASSWORD);
        user = new User();
        user.addRole(USER_ROLE);
        user.setEmail(EMAIL);
        userService.createWithPasswordHash(user, passwordHash);

        UserCredentialsView credentialsView = new UserCredentialsView(user.getEmail(), OLD_PASSWORD);
        MvcResult result = mockMvc.perform(post(ACQUIRE_TOKEN_PATH)
                .contentType(CONTENT_TYPE)
                .content(ModelMapper.convertToJsonString(credentialsView)))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        int jwtTokenBeginIndex = response.indexOf(JWT_JSON_SIGN) + JWT_JSON_SIGN.length();
        int jwtTokenEndIndex = response.length() - 2;
        token = response.substring(jwtTokenBeginIndex, jwtTokenEndIndex);
    }

    @After
    public void tearDown() throws Exception {
        userService.delete(user.getId());
    }

    @Test
    public void shouldReturnBadRequestWhenPasswordAreTheSame() throws Exception {
        PasswordChangeRequestView samePasswords = new PasswordChangeRequestView(OLD_PASSWORD, OLD_PASSWORD);

        mockMvc.perform(post(ApiUrl.PASSWORDS_PATH)
                .header(AUTHORIZATION_HEADER, TOKEN_PREFIX + token)
                .contentType(CONTENT_TYPE)
                .content(ModelMapper.convertToJsonString(samePasswords)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestWhenPasswordsDoNotMatch() throws Exception {
        PasswordChangeRequestView notMatchingPasswords = new PasswordChangeRequestView(NEW_PASSWORD, NEW_PASSWORD);

        mockMvc.perform(post(ApiUrl.PASSWORDS_PATH)
                .header(AUTHORIZATION_HEADER, TOKEN_PREFIX + token)
                .contentType(CONTENT_TYPE)
                .content(ModelMapper.convertToJsonString(notMatchingPasswords)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldChangePassword() throws Exception {
        PasswordChangeRequestView passwords = new PasswordChangeRequestView(OLD_PASSWORD, NEW_PASSWORD);
        String responseMessage = messageSource.getMessage(
                "passwordChangeSuccessful", null, LocaleContextHolder.getLocale());

        mockMvc.perform(post(ApiUrl.PASSWORDS_PATH)
                .header(AUTHORIZATION_HEADER, TOKEN_PREFIX + token)
                .contentType(CONTENT_TYPE)
                .content(ModelMapper.convertToJsonString(passwords)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(jsonPath("message", is(responseMessage)));

        UserCredentialsView credentialsView = new UserCredentialsView(user.getEmail(), NEW_PASSWORD);
        mockMvc.perform(post(ACQUIRE_TOKEN_PATH)
                .contentType(CONTENT_TYPE)
                .content(ModelMapper.convertToJsonString(credentialsView)))
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("jwt", notNullValue()));
    }
}
