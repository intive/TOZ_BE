package com.intive.patronage.toz.tokens;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intive.patronage.toz.tokens.model.view.UserCredentialsView;
import com.intive.patronage.toz.users.model.db.User;
import com.intive.patronage.toz.users.model.enumerations.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;

import static com.intive.patronage.toz.config.ApiUrl.ACQUIRE_TOKEN_PATH;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "jwt.secret-base64=c2VjcmV0"
)
public class TokenControllerTest {

    private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;

    private static final String EMAIL = "user@mail.com";
    private static final String PASSWORD = "Password";
    private static final String EMAIL_CLAIM_NAME = "email";
    private static final String SCOPES_CLAIM_NAME = "scopes";
    private static final long EXPIRATION_TIME = 5;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Value("${jwt.secret-base64}")
    private String secret;

    @Mock
    private TokensService tokensService;

    private MockMvc mockMvc;
    private UserCredentialsView credentialsView;

    private static String convertToJsonString(Object value) {
        try {
            return new ObjectMapper().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new TokensController(tokensService)).build();

        credentialsView = new UserCredentialsView(EMAIL, PASSWORD);
    }

    @Test
    public void shouldReturnCreatedWhenProperUserAndPassword() throws Exception {
        when(tokensService.isUserAuthenticated(any(String.class), any(String.class))).thenReturn(true);
        this.mockMvc.perform(post(ACQUIRE_TOKEN_PATH)
                .contentType(CONTENT_TYPE)
                .content(convertToJsonString(credentialsView)))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldThrowExceptionWhenImproperUserAndPassword() throws Exception {
        expectedException.expectCause(isA(BadCredentialsException.class));

        when(tokensService.isUserAuthenticated(any(String.class), any(String.class))).thenReturn(false);
        this.mockMvc.perform(post(ACQUIRE_TOKEN_PATH)
                .contentType(CONTENT_TYPE)
                .content(convertToJsonString(credentialsView)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldReturnToken() throws Exception {
        User user = new User();
        user.setRole(Role.VOLUNTEER);
        user.setEmail(EMAIL);
        user.setPassword(PASSWORD);
        String token = generateToken(user);

        when(tokensService.isUserAuthenticated(any(String.class), any(String.class))).thenReturn(true);
        when(tokensService.getToken(EMAIL)).thenReturn(token);

        mockMvc.perform(post(ACQUIRE_TOKEN_PATH)
                .contentType(CONTENT_TYPE)
                .content(convertToJsonString(credentialsView)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("jwt", is(token)));
    }

    private String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim(EMAIL_CLAIM_NAME, user.getEmail())
                .claim(SCOPES_CLAIM_NAME, Collections.singleton(user.getRole()))
                .setIssuedAt(new Date(Instant.now().toEpochMilli()))
                .setExpiration(new Date(Instant.now().plus(EXPIRATION_TIME, ChronoUnit.MINUTES).toEpochMilli()))
                .signWith(SignatureAlgorithm.HS512, TextCodec.BASE64.decode(secret))
                .compact();
    }
}
