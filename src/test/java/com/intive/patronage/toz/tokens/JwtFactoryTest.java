package com.intive.patronage.toz.tokens;

import com.intive.patronage.toz.users.model.db.User;
import com.intive.patronage.toz.users.model.enumerations.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.TextCodec;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
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
        user.setRole(ROLE);

        jwtFactory = new JwtFactory(EXPIRATION_TIME, SECRET);
    }

    @Test
    public void shouldGenerateValidToken() throws Exception {
        String token = jwtFactory.generateToken(user);

        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(TextCodec.BASE64.decode(SECRET))
                .parseClaimsJws(token);

        final String email = claims.getBody().get("email", String.class);
        final List<String> scopes = claims.getBody().get("scopes", List.class);

        assertEquals(email, EMAIL);
        assertTrue(scopes.contains(ROLE.toString()));
    }
}
