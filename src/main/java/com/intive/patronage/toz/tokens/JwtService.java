package com.intive.patronage.toz.tokens;

import com.intive.patronage.toz.users.model.db.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
class JwtService {

    @Value("${jwt.expiration-time-minutes}")
    private long expirationTime;

    @Value("${jwt.secret-base64}")
    private String secret;

    String generateToken(User user) {

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("scope", user.getRole())
                .claim("email", user.getEmail())
                .setIssuedAt(new Date(Instant.now().toEpochMilli()))
                .setExpiration(new Date(Instant.now().plus(expirationTime, ChronoUnit.MINUTES).toEpochMilli()))
                .signWith(SignatureAlgorithm.HS256, TextCodec.BASE64.decode(secret))
                .compact();
    }
}
