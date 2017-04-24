package com.intive.patronage.toz.tokens;

import com.intive.patronage.toz.users.UsersService;
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

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class TokensServiceTest {

    private static final String EXPECTED_EMAIL = "test@wp.pl";
    private static final String EXPECTED_PASSWORD_HASH = "123456";

    private TokensService tokensService;
    @Mock
    private UsersService usersService;
    @Mock
    private JwtFactory jwtFactory;

    private PasswordEncoder passwordEncoder;
    private User user;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        SecureRandom random = new SecureRandom();
        random.nextBytes(new byte[20]);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10, random);

        tokensService = new TokensService(usersService, passwordEncoder, jwtFactory);

        user = new User();
        user.setEmail(EXPECTED_EMAIL);
        user.setPasswordHash(passwordEncoder.encode(EXPECTED_PASSWORD_HASH));
    }

    @Test
    public void login() throws Exception {
        when(usersService.findOneByEmail(EXPECTED_EMAIL)).thenReturn(user);
        assertTrue(tokensService.isUserAuthenticated(EXPECTED_EMAIL, EXPECTED_PASSWORD_HASH));
    }

}
