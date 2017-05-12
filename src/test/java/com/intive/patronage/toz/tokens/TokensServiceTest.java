package com.intive.patronage.toz.tokens;

import com.intive.patronage.toz.users.model.db.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.SecureRandom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class TokensServiceTest {

    private static final String EMAIL = "test@wp.pl";
    private static final String CORRECT_PASSWORD = "123456";
    private static final String WRONG_PASSWORD = "WrongPa55word";
    private static final String VALID_TOKEN = "valid.token.1234";
    private static final long EXPIRATION_TIME = 5;

    @Mock
    private JwtFactory jwtFactory;

    private TokensService tokensService;
    private User user;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        SecureRandom random = new SecureRandom();
        random.nextBytes(new byte[20]);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10, random);

        tokensService = new TokensService(EXPIRATION_TIME, passwordEncoder, jwtFactory);

        user = new User();
        user.setEmail(EMAIL);
        user.setPasswordHash(passwordEncoder.encode(CORRECT_PASSWORD));
    }

    @Test
    public void shouldAuthenticateUserWithCorrectPassword() throws Exception {
        assertTrue(tokensService.isUserAuthenticated(user, CORRECT_PASSWORD));
    }

    @Test
    public void shouldNotAuthenticateUserWithWrongPassword() throws Exception {
        assertFalse(tokensService.isUserAuthenticated(user, WRONG_PASSWORD));
    }

    @Test
    public void shouldReturnToken() throws Exception {
        when(jwtFactory.generateToken(user, EXPIRATION_TIME)).thenReturn(VALID_TOKEN);

        final String token = tokensService.getToken(user);

        assertThat(token).isEqualTo(VALID_TOKEN);
    }
}
