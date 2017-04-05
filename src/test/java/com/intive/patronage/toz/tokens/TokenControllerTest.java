package com.intive.patronage.toz.tokens;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intive.patronage.toz.users.model.view.UserView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.intive.patronage.toz.config.ApiUrl.TOKENS_PATH;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class TokenControllerTest {

    private static final MediaType CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8;

    @Mock
    private TokensService tokensService;
    private MockMvc mockMvc;

    private UserView userView;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new TokensController(tokensService)).build();

        userView = new UserView();
        userView.setPassword("123456");
        userView.setEmail("test@poczta.pl");
    }

    @Test
    public void shouldReturnOkWhenProperUserAndPassword() throws Exception {
        when(tokensService.login(any(UserView.class))).thenReturn(true);
        this.mockMvc.perform(post(String.format("%s/%s", TOKENS_PATH, "acquire"))
                .contentType(CONTENT_TYPE)
                .content(convertToJsonString(userView)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnForbiddenWhenImproperUserAndPassword() throws Exception {
        when(tokensService.login(any(UserView.class))).thenReturn(false);
        this.mockMvc.perform(post(String.format("%s/%s", TOKENS_PATH, "acquire"))
                .contentType(CONTENT_TYPE)
                .content(convertToJsonString(userView)))
                .andExpect(status().isForbidden());
    }

    private static String convertToJsonString(Object value) {
        try {
            return new ObjectMapper().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
