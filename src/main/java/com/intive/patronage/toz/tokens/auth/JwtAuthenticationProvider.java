package com.intive.patronage.toz.tokens.auth;

import com.intive.patronage.toz.error.exception.JwtAuthenticationException;
import com.intive.patronage.toz.tokens.model.UserContext;
import com.intive.patronage.toz.users.model.enumerations.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final String secret;

    JwtAuthenticationProvider() {
        secret = null;
    }

    public JwtAuthenticationProvider(String secret) {
        this.secret = secret;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String token = (String) authentication.getCredentials();

        if (token == null) {
            List<GrantedAuthority> authorities =
                    Collections.singletonList(new SimpleGrantedAuthority(Role.ANONYMOUS.toString()));
            return new JwtAuthenticationToken(null, authorities);
        }

        Jws<Claims> claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(TextCodec.BASE64.decode(secret))
                    .parseClaimsJws(token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("Invalid token");
        } catch (SignatureException e) {
            throw new JwtAuthenticationException("Invalid signature");
        } catch (ExpiredJwtException e) {
            throw new JwtAuthenticationException("Token expired");
        }

        final UUID userID = UUID.fromString(claims.getBody().getSubject());
        final String email = claims.getBody().get("email", String.class);
        final List<String> scopes = claims.getBody().get("scopes", List.class);

        Set<GrantedAuthority> authorities = scopes.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        final UserContext userContext = new UserContext(userID, email, authorities);
        return new JwtAuthenticationToken(userContext, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
