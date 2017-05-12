package com.intive.patronage.toz.tokens;

import com.intive.patronage.toz.environment.ApiProperties;
import com.intive.patronage.toz.tokens.model.view.UserCredentialsView;
import com.intive.patronage.toz.users.UserService;
import com.intive.patronage.toz.users.model.db.User;
import com.intive.patronage.toz.util.ModelMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.stream.Collectors;

import static com.intive.patronage.toz.config.ApiUrl.ACQUIRE_TOKEN_PATH;
import static com.intive.patronage.toz.config.ApiUrl.TOKENS_PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
@TestPropertySource(
        properties = {
                ApiProperties.JWT_SECRET_BASE64,
                ApiProperties.SUPER_ADMIN_PASSWORD
        }
)
@ActiveProfiles("test")
public class TokenControllerTest {

    static final String EMAIL = "user@mail.com";
    static final String AUTHORIZATION_HEADER = "Authorization";
    static final String TOKEN_PREFIX = "Bearer ";
    static final User.Role ROLE_ADMIN = User.Role.TOZ;
    private static final String PASSWORD = "Password";
    private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;

    @Value("${jwt.secret-base64}")
    private String secret;

    @Value("${jwt.expiration-time-minutes}")
    private long expirationTime;

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtFactory jwtFactory;

    @Autowired
    private JwtParser jwtParser;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserCredentialsView credentialsView;
    private User user;

    @Before
    public void setUp() throws Exception {
        final String passwordHash = passwordEncoder.encode(PASSWORD);
        user = new User();
        user.addRole(ROLE_ADMIN);
        user.setEmail(EMAIL);
        user.setPasswordHash(passwordHash);
        userService.createWithPasswordHash(user, passwordHash);

        credentialsView = new UserCredentialsView(EMAIL, PASSWORD);
    }

    @After
    public void tearDown() throws Exception {
        userService.delete(user.getId());
    }

    @Test
    public void shouldReturnCreatedWhenProperUserAndPassword() throws Exception {
        this.mockMvc.perform(post(ACQUIRE_TOKEN_PATH)
                .contentType(CONTENT_TYPE)
                .content(ModelMapper.convertToJsonString(credentialsView)))
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldReturnUnauthorizedWhenImproperUserAndPassword() throws Exception {
        UserCredentialsView wrongCredentials = new UserCredentialsView(EMAIL, "WrongPass");
        this.mockMvc.perform(post(ACQUIRE_TOKEN_PATH)
                .contentType(CONTENT_TYPE)
                .content(ModelMapper.convertToJsonString(wrongCredentials)))
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldReturnNotFoundWhenUserDoesNotExists() throws Exception {
        UserCredentialsView nonExisting = new UserCredentialsView("other@mail.com", PASSWORD);
        this.mockMvc.perform(post(ACQUIRE_TOKEN_PATH)
                .contentType(CONTENT_TYPE)
                .content(ModelMapper.convertToJsonString(nonExisting)))
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnValidToken() throws Exception {
        final List<String> userRoles = user.getRoles().stream()
                .map(User.Role::name)
                .collect(Collectors.toList());

        final MvcResult result = mockMvc.perform(post(ACQUIRE_TOKEN_PATH)
                .contentType(CONTENT_TYPE)
                .content(ModelMapper.convertToJsonString(credentialsView)))
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("userId", is(user.getId().toString())))
                .andExpect(jsonPath("email", is(user.getEmail())))
                .andExpect(jsonPath("roles", is(userRoles)))
                .andExpect(jsonPath("jwt", notNullValue()))
                .andReturn();

        final String response = result.getResponse().getContentAsString();
        final int tokenBeginIndex = response.lastIndexOf("jwt") + "jwt\":\"".length();
        final int tokenEndIndex = response.length() - 2;
        final String token = response.substring(tokenBeginIndex, tokenEndIndex);

        jwtParser.parse(token);
        assertThat(jwtParser.getUserId())
                .isEqualTo(user.getId());
        assertThat(jwtParser.getEmail())
                .isEqualTo(user.getEmail());
        assertThat(jwtParser.getTokenExpirationDate())
                .isInTheFuture();
        assertThat(jwtParser.getScopes())
                .hasSize(1)
                .containsExactly(ROLE_ADMIN.toString());
    }

    @Profile("dev")
    @Test
    public void shouldReturnUserContext() throws Exception {
        mockMvc.perform(get(TOKENS_PATH + "/whoami")
                .header(AUTHORIZATION_HEADER,
                        TOKEN_PREFIX + jwtFactory.generateToken(user, expirationTime)))
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("userId", is(user.getId().toString())))
                .andExpect(jsonPath("email", is(user.getEmail())))
                .andExpect(jsonPath("authorities[0]", is(ROLE_ADMIN.toString())));
    }

    @Profile("dev")
    @Test
    public void shouldReturnUnauthorizedWhenNoTokenInHeader() throws Exception {
        mockMvc.perform(get(TOKENS_PATH + "/whoami")
                .header(AUTHORIZATION_HEADER, TOKEN_PREFIX))
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(status().isUnauthorized());
    }
}
