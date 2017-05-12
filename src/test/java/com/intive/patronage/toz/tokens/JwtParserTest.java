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

import static org.assertj.core.api.Assertions.assertThat;
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
        final JwtFactory jwtFactory = new JwtFactory("wrongSecret");
        final String badToken = jwtFactory.generateToken(user, EXPIRATION_TIME);

        when(messageSource.getMessage(eq("invalidSignature"), any(), any(Locale.class)))
                .thenReturn(INVALID_SIGNATURE_MESSAGE);

        try {
            jwtParser.parse(badToken);
        } catch (JwtAuthenticationException e) {
            assertThat(e.getMessage())
                    .isEqualTo(INVALID_SIGNATURE_MESSAGE);
        }
    }

    @Test
    public void shouldThrowExceptionWhenTokenExpired() throws Exception {
        final JwtFactory jwtFactory = new JwtFactory(SECRET);
        final String expiredToken = jwtFactory.generateToken(user, -5);

        when(messageSource.getMessage(eq("tokenExpired"), any(), any(Locale.class)))
                .thenReturn(EXPIRED_TOKEN_MESSAGE);

        try {
            jwtParser.parse(expiredToken);
        } catch (JwtAuthenticationException e) {
            assertThat(e.getMessage())
                    .isEqualTo(EXPIRED_TOKEN_MESSAGE);
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
            assertThat(e.getMessage())
                    .isEqualTo(INVALID_TOKEN_MESSAGE);
        }
    }

    @Test
    public void shouldReturnValidToken() throws Exception {
        final JwtFactory jwtFactory = new JwtFactory(SECRET);
        final String token = jwtFactory.generateToken(user, EXPIRATION_TIME);
        final List<String> userRoles = user.getRoles().stream()
                .map(User.Role::name)
                .collect(Collectors.toList());

        jwtParser.parse(token);

        assertThat(jwtParser.getUserId())
                .isEqualTo(user.getId());
        assertThat(jwtParser.getEmail())
                .isEqualTo(user.getEmail());
        assertThat(jwtParser.getTokenExpirationDate())
                .isInTheFuture();
        assertThat(jwtParser.getScopes())
                .hasSize(1)
                .containsAll(userRoles);
    }
}
