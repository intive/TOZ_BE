package com.intive.patronage.toz.tokens;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intive.patronage.toz.users.model.view.UserView;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.intive.patronage.toz.config.ApiUrl.ACQUIRE_TOKEN_PATH;
import static com.intive.patronage.toz.config.ApiUrl.TOKENS_PATH;
import static org.hamcrest.CoreMatchers.isA;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class TokenControllerTest {

    private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private TokensService tokensService;
    @Mock
    private JwtFactory jwtFactory;

    private MockMvc mockMvc;
    private UserView userView;

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
        mockMvc = MockMvcBuilders.standaloneSetup(new TokensController(tokensService, jwtFactory)).build();

        userView = new UserView();
        userView.setPassword("123456");
        userView.setEmail("test@poczta.pl");
    }

    @Test
    public void shouldReturnOkWhenProperUserAndPassword() throws Exception {
        when(tokensService.isUserAuthenticated(any(String.class), any(String.class))).thenReturn(true);
        this.mockMvc.perform(post(String.format("%s%s", TOKENS_PATH, ACQUIRE_TOKEN_PATH))
                .contentType(CONTENT_TYPE)
                .content(convertToJsonString(userView)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldThrowExceptionWhenImproperUserAndPassword() throws Exception {
        expectedException.expectCause(isA(BadCredentialsException.class));

        when(tokensService.isUserAuthenticated(any(String.class), any(String.class))).thenReturn(false);
        this.mockMvc.perform(post(String.format("%s%s", TOKENS_PATH, ACQUIRE_TOKEN_PATH))
                .contentType(CONTENT_TYPE)
                .content(convertToJsonString(userView)))
                .andExpect(status().isForbidden());
    }
}
