package com.intive.patronage.toz.tokens;

import com.intive.patronage.toz.users.UserService;
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
    private static final String EXPECTED_PASSWORD = "123456";

    private TokensService tokensService;
    @Mock
    private UserService userService;
    @Mock
    private JwtFactory jwtFactory;

    private PasswordEncoder passwordEncoder;
    private User user;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        SecureRandom random = new SecureRandom();
        random.nextBytes(new byte[20]);
        passwordEncoder = new BCryptPasswordEncoder(10, random);

        tokensService = new TokensService(userService, passwordEncoder, jwtFactory);

        user = new User();
        user.setEmail(EXPECTED_EMAIL);
        user.setPassword(EXPECTED_PASSWORD);
        // TODO remove line above and uncomment one below once database stores hashed passwords
        //user.setPassword(passwordEncoder.encode(EXPECTED_PASSWORD));
    }

    @Test
    public void login() throws Exception {
        when(userService.findOneByEmail(EXPECTED_EMAIL)).thenReturn(user);
        assertTrue(tokensService.isUserAuthenticated(EXPECTED_PASSWORD, EXPECTED_EMAIL));
    }

}
