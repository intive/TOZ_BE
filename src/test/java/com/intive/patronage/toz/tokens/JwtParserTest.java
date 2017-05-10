package com.intive.patronage.toz.tokens;

import com.intive.patronage.toz.error.exception.JwtAuthenticationException;
import com.intive.patronage.toz.users.model.db.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class JwtParserTest {

    private static final String EMAIL = "user@mail.com";
    private static final User.Role VOLUNTEER_ROLE = User.Role.VOLUNTEER;
    private static final String SECRET = "c2VjcmV0";
    private static final long EXPIRATION_TIME = 5;
    private static final String EXPIRED_TOKEN_MESSAGE = "Token expired";
    private static final String INVALID_SIGNATURE_MESSAGE = "Invalid signature";
    private static final String INVALID_TOKEN_MESSAGE = "Invalid token";

    private JwtParser jwtParser;
    private User user;

    @MockBean
    private MessageSource messageSource;

    @Before
    public void setUp() throws Exception {
        jwtParser = new JwtParser(messageSource, SECRET);

        user = new User();
        user.setEmail(EMAIL);
        user.addRole(VOLUNTEER_ROLE);
    }

    @Test
    public void shouldThrowExceptionWhenWrongSecret() throws Exception {
        final JwtFactory jwtFactory = new JwtFactory(EXPIRATION_TIME, "wrongSecret");
        final String badToken = jwtFactory.generateToken(user);

        when(messageSource.getMessage(eq("invalidSignature"), any(), any(Locale.class)))
                .thenReturn(INVALID_SIGNATURE_MESSAGE);

        try {
            jwtParser.parse(badToken);
        } catch (JwtAuthenticationException e) {
            assertEquals(e.getMessage(), INVALID_SIGNATURE_MESSAGE);
        }
    }

    @Test
    public void shouldThrowExceptionWhenTokenExpired() throws Exception {
        final JwtFactory jwtFactory = new JwtFactory(-5, SECRET);
        final String expiredToken = jwtFactory.generateToken(user);

        when(messageSource.getMessage(eq("tokenExpired"), any(), any(Locale.class)))
                .thenReturn(EXPIRED_TOKEN_MESSAGE);

        try {
            jwtParser.parse(expiredToken);
        } catch (JwtAuthenticationException e) {
            assertEquals(e.getMessage(), EXPIRED_TOKEN_MESSAGE);
        }
    }

    @Test
    public void shouldThrowExceptionWhenInvalidToken() throws Exception {
        final String invalidToken = "this.isInvalid.token";

        when(messageSource.getMessage(eq("invalidToken"), any(), any(Locale.class)))
                .thenReturn(INVALID_TOKEN_MESSAGE);

        try {
            jwtParser.parse(invalidToken);
        } catch (JwtAuthenticationException e) {
            assertEquals(e.getMessage(), INVALID_TOKEN_MESSAGE);
        }
    }

    @Test
    public void shouldReturnValidToken() throws Exception {
        final JwtFactory jwtFactory = new JwtFactory(EXPIRATION_TIME, SECRET);
        final String token = jwtFactory.generateToken(user);

        jwtParser.parse(token);

        assertEquals(jwtParser.getUserId(), user.getId());
        assertEquals(jwtParser.getEmail(), user.getEmail());
        final List<String> userRoles = user.getRoles().stream()
                .map(User.Role::name)
                .collect(Collectors.toList());
        assertTrue(jwtParser.getScopes().containsAll(userRoles));
    }
}
