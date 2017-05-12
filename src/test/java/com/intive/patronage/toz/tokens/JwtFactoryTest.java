package com.intive.patronage.toz.tokens;

import com.intive.patronage.toz.users.model.db.Role;
import com.intive.patronage.toz.users.model.db.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.TextCodec;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class JwtFactoryTest {

    private static final String EMAIL = "user@mail.com";
    private static final Role ROLE = Role.VOLUNTEER;
    private static final String SECRET = "c2VjcmV0";
    private static final long EXPIRATION_TIME = 5;

    private User user;
    private JwtFactory jwtFactory;

    @Before
    public void setUp() throws Exception {
        user = new User();
        user.setEmail(EMAIL);
        user.addRole(ROLE);

        jwtFactory = new JwtFactory(SECRET);
    }

    @Test
    public void shouldGenerateValidToken() throws Exception {
        String token = jwtFactory.generateToken(user, EXPIRATION_TIME);

        Claims claimsBody = Jwts.parser()
                .setSigningKey(TextCodec.BASE64.decode(SECRET))
                .parseClaimsJws(token)
                .getBody();

        assertThat(claimsBody.get("email", String.class))
                .isEqualTo(EMAIL);
        assertThat(claimsBody.get("scopes", List.class))
                .hasSize(1)
                .containsExactly(ROLE.toString());
    }
}
