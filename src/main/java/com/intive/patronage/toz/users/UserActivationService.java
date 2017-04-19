package com.intive.patronage.toz.users;


import com.intive.patronage.toz.error.exception.JwtAuthenticationException;
import com.intive.patronage.toz.users.model.db.User;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class UserActivationService {


    private static final String EMAIL_CLAIM_NAME = "email";
    private static final String FORENAME_CLAIM_NAME = "firstname";
    private static final String SURNAME_CLAIM_NAME = "lastname";
    private static final String PASSWORD_CLAIM_NAME = "password";

    private final long expirationTime;
    private final String secret;

    private UserRepository userRepository;


    @Autowired
    public UserActivationService(
            UserRepository userRepository,
            @Value("${jwt.activation.expiration-time-minutes}") long expirationTime,
            @Value("${jwt.secret-base64}") String secret) {
        this.userRepository = userRepository;
        this.expirationTime = expirationTime;
        this.secret = secret;
    }

    public String generateUserActivationToken(User user ){
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim(EMAIL_CLAIM_NAME, user.getEmail())
                .claim(FORENAME_CLAIM_NAME, user.getForename())
                .claim(SURNAME_CLAIM_NAME, user.getSurname())
                .claim(PASSWORD_CLAIM_NAME, user.getPassword())
                .setIssuedAt(new Date(Instant.now().toEpochMilli()))
                .setExpiration(new Date(Instant.now().plus(expirationTime, ChronoUnit.MINUTES).toEpochMilli()))
                .signWith(SignatureAlgorithm.HS512, TextCodec.BASE64.decode(secret))
                .compact();
    }
    public User checkActivationToken(String token) {

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
        if (userRepository.findByEmail(claims.getBody().get(EMAIL_CLAIM_NAME, String.class)) != null){
            throw new JwtAuthenticationException("User alread exists");
        }
        User user = new User();
        user.setEmail(claims.getBody().get(EMAIL_CLAIM_NAME, String.class));
        user.setPassword(claims.getBody().get(PASSWORD_CLAIM_NAME, String.class));
        user.setForename(claims.getBody().get(FORENAME_CLAIM_NAME, String.class));
        user.setSurname(claims.getBody().get(SURNAME_CLAIM_NAME, String.class));

        return user;
    }
}
