package com.intive.patronage.toz.tokens;

import com.intive.patronage.toz.environment.ApiProperties;
import com.intive.patronage.toz.tokens.model.view.UserCredentialsView;
import com.intive.patronage.toz.users.UserService;
import com.intive.patronage.toz.users.model.db.User;
import com.intive.patronage.toz.util.ModelMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.TextCodec;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static com.intive.patronage.toz.config.ApiUrl.ACQUIRE_TOKEN_PATH;
import static com.intive.patronage.toz.config.ApiUrl.TOKENS_PATH;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {ApiProperties.JWT_SECRET_BASE64, ApiProperties.SUPER_ADMIN_PASSWORD}
)
@ActiveProfiles("test")
public class TokenControllerTest {

    private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;
    private static final String EMAIL = "user@mail.com";
    private static final String PASSWORD = "Password";
    private static final String EMAIL_CLAIM_NAME = "email";
    private static final String SCOPES_CLAIM_NAME = "scopes";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final User.Role ROLE = User.Role.TOZ;

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
    private PasswordEncoder passwordEncoder;

    private UserCredentialsView credentialsView;
    private User user;

    @Before
    public void setUp() throws Exception {
        user = new User();
        user.addRole(ROLE);
        user.setEmail(EMAIL);
        user.setPasswordHash(passwordEncoder.encode(PASSWORD));
        userService.createWithPassword(user, PASSWORD);

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
        MvcResult result = mockMvc.perform(post(ACQUIRE_TOKEN_PATH)
                .contentType(CONTENT_TYPE)
                .content(ModelMapper.convertToJsonString(credentialsView)))
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("jwt", notNullValue()))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        String token = response.substring("{\"jwt\":\"".length(), response.length() - 2);

        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(TextCodec.BASE64.decode(secret))
                .parseClaimsJws(token);

        assertEquals(claims.getBody().getSubject(), user.getId().toString());
        assertEquals(claims.getBody().get(EMAIL_CLAIM_NAME, String.class), user.getEmail());
        final List<String> scopes = claims.getBody().get(SCOPES_CLAIM_NAME, List.class);
        assertTrue(scopes.contains(ROLE.toString()));
    }

    @Profile("dev")
    @Test
    public void shouldReturnUserContext() throws Exception {
        mockMvc.perform(get(TOKENS_PATH + "/whoami")
                .header(AUTHORIZATION_HEADER, TOKEN_PREFIX + jwtFactory.generateToken(user)))
                .andExpect(content().contentType(CONTENT_TYPE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("userId", is(user.getId().toString())))
                .andExpect(jsonPath("email", is(user.getEmail())))
                .andExpect(jsonPath("authorities[0].authority", is(ROLE.toString())));
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
