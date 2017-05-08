package com.intive.patronage.toz.tokens;

import com.intive.patronage.toz.error.exception.JwtAuthenticationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class JwtParser {

    private final MessageSource messageSource;
    private final String secret;
    private Jws<Claims> claims;

    @Autowired
    public JwtParser(MessageSource messageSource, @Value("${jwt.secret-base64}") String secret) {
        this.secret = secret;
        this.messageSource = messageSource;
    }

    public void parse(String token) {
        try {
            claims = Jwts.parser()
                    .setSigningKey(TextCodec.BASE64.decode(secret))
                    .parseClaimsJws(token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            String message = messageSource.getMessage("invalidToken",
                    null, LocaleContextHolder.getLocale());
            throw new JwtAuthenticationException(message);
        } catch (SignatureException e) {
            String message = messageSource.getMessage("invalidSignature",
                    null, LocaleContextHolder.getLocale());
            throw new JwtAuthenticationException(message);
        } catch (ExpiredJwtException e) {
            String message = messageSource.getMessage("tokenExpired",
                    null, LocaleContextHolder.getLocale());
            throw new JwtAuthenticationException(message);
        }
    }

    public UUID getUserId() {
        return UUID.fromString(claims.getBody().getSubject());
    }

    public String getEmail() {
        return claims.getBody().get("email", String.class);
    }

    public List<String> getScopes() {
        return claims.getBody().get("scopes", List.class);
    }
}
