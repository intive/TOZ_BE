package com.intive.patronage.toz.tokens;

import com.intive.patronage.toz.users.model.view.UserCredentials;
import com.intive.patronage.toz.util.ModelMapper;
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
    private UserCredentials userCredentials;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new TokensController(tokensService)).build();
        userCredentials = new UserCredentials();
        userCredentials.setPassword("123456");
        userCredentials.setEmail("test@poczta.pl");
    }

    @Test
    public void shouldReturnOkWhenProperUserAndPassword() throws Exception {
        when(tokensService.isUserAuthenticated(any(String.class), any(String.class))).thenReturn(true);
        this.mockMvc.perform(post(String.format("%s/%s", TOKENS_PATH, "acquire"))
                .contentType(CONTENT_TYPE)
                .content(ModelMapper.convertToJsonString(userCredentials)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnForbiddenWhenImproperUserAndPassword() throws Exception {
        when(tokensService.isUserAuthenticated(any(String.class), any(String.class))).thenReturn(false);
        this.mockMvc.perform(post(String.format("%s/%s", TOKENS_PATH, "acquire"))
                .contentType(CONTENT_TYPE)
                .content(ModelMapper.convertToJsonString(userCredentials)))
                .andExpect(status().isForbidden());
    }

}
