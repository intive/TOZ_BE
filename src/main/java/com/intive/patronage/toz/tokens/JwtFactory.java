package com.intive.patronage.toz.tokens;

import com.intive.patronage.toz.base.model.PersonalData;
import com.intive.patronage.toz.users.model.db.RoleEntity;
import com.intive.patronage.toz.users.model.db.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import org.hibernate.event.internal.DefaultPersistOnFlushEventListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class JwtFactory {

    public static final String EMAIL_CLAIM_NAME = "email";
    public static final String SCOPES_CLAIM_NAME = "scopes";

    private final String secret;

    public JwtFactory(@Value("${jwt.secret-base64}") String secret) {
        this.secret = secret;
    }


    private static List<User.Role> getRolesFromPersonalData(PersonalData personalData) {
        List<PersonalData.Role> roles = new ArrayList<>();
        for (RoleEntity entity : personalData.getRoles()) {
            roles.add(entity.getRole());
        }
        return roles;
    }
    private String generateTokenData(PersonalData personalData, long expirationTimeInMinutes){
        return Jwts.builder()
                .setSubject(personalData.getId().toString())
                .claim(EMAIL_CLAIM_NAME, personalData.getEmail())
                .claim(SCOPES_CLAIM_NAME, getRolesFromPersonalData(personalData))
                .setIssuedAt(new Date(Instant.now().toEpochMilli()))
                .setExpiration(new Date(Instant.now().plus(expirationTimeInMinutes, ChronoUnit.MINUTES).toEpochMilli()))
                .signWith(SignatureAlgorithm.HS512, TextCodec.BASE64.decode(secret))
                .compact();
    }

    public String generateTokenWithSpecifiedExpirationTime(PersonalData personalData, long expirationTimeInMinutes){
        return generateTokenData(personalData, expirationTimeInMinutes);
    }
}
