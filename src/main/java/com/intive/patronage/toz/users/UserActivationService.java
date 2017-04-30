package com.intive.patronage.toz.users;


import com.intive.patronage.toz.error.exception.JwtAuthenticationException;
import com.intive.patronage.toz.tokens.auth.JwtAuthenticationProvider;
import com.intive.patronage.toz.users.model.db.User;
import com.intive.patronage.toz.tokens.JwtFactory;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class UserActivationService {

    private final long expirationTime;
    private final String secret;
    private UserRepository userRepository;

    @Autowired
    public UserActivationService(
            UserRepository userRepository,
            @Value("${jwt.email.activation.expiration-time-minutes}") Long expirationTime,
            @Value("${jwt.secret-base64}") String secret) {
        this.userRepository = userRepository;
        this.expirationTime = expirationTime;
        this.secret = secret;
    }

    public User checkActivationToken(String token) {
        JwtAuthenticationProvider jwtap = new JwtAuthenticationProvider(secret);
        Jws<Claims> claims = jwtap.parseToken(token);

        if (userRepository.findByEmail(claims.getBody().get(JwtFactory.EMAIL_CLAIM_NAME, String.class)) != null){
            throw new JwtAuthenticationException("User already exists");
        }
        User user = new User();
        user.setEmail(claims.getBody().get(JwtFactory.EMAIL_CLAIM_NAME, String.class));
        user.setSurname(claims.getBody().get(JwtFactory.SURNAME_CLAIM_NAME, String.class));
        user.setPhoneNumber(claims.getBody().get(JwtFactory.PHONENUMBER_CLAIM_NAME, String.class));

        return user;
    }
}
