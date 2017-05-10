package com.intive.patronage.toz.users;


import com.intive.patronage.toz.error.exception.JwtAuthenticationException;
import com.intive.patronage.toz.mail.MailService;
import com.intive.patronage.toz.mail.MailTemplatesService;
import com.intive.patronage.toz.proposals.ProposalRepository;
import com.intive.patronage.toz.proposals.model.Proposal;
import com.intive.patronage.toz.tokens.auth.JwtAuthenticationProvider;
import com.intive.patronage.toz.users.model.db.User;
import com.intive.patronage.toz.tokens.JwtFactory;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;


@Service
public class UserActivationService {

    private final String secret;

    @Value("${jwt.email.activation.expiration-time-minutes}")
    private long expirationTime;


    private JwtFactory jwtFactory;
    private UserRepository userRepository;
    private ProposalRepository proposalRepository;
    private MailTemplatesService mailTemplatesService;
    private MailService mailService;
    private PasswordEncoder passwordEncoder;

    private String mailSubject;
    private String registerUrl;


    @Autowired
    public UserActivationService(
            JwtFactory jwtFactory,
            UserRepository userRepository,
            ProposalRepository proposalRepository,
            MailTemplatesService mailTemplatesService,
            PasswordEncoder passwordEncoder,
            @Value("${jwt.secret-base64}") String secret,
            @Value("${mail.register.subject}") String mailSubject,
            @Value("${mail.register.url}") String registerUrl
        ) {
        this.secret = secret;
        this.mailSubject = mailSubject;
        this.registerUrl = registerUrl;
        this.jwtFactory = jwtFactory;
        this.userRepository = userRepository;
        this.proposalRepository = proposalRepository;
        this.mailTemplatesService = mailTemplatesService;
        this.passwordEncoder = passwordEncoder;
    }

    public void sendActivationEmail(Proposal proposal) throws IOException, MessagingException {
        String token = jwtFactory.generateTokenWithSpecifiedExpirationTime(proposal, expirationTime);
        String mailBody = mailTemplatesService.getRegistrationTemplate(registerUrl, token);
        mailService.sendMail(mailSubject, mailBody, proposal.getEmail());
    }

    public User checkActivationToken(String token, String password) {
        JwtAuthenticationProvider jwtap = new JwtAuthenticationProvider(secret);
        Jws<Claims> claims = jwtap.parseToken(token);

        String email = claims.getBody().get(JwtFactory.EMAIL_CLAIM_NAME, String.class);

        System.out.println(email);
        if (userRepository.findByEmail(email) != null){
            throw new JwtAuthenticationException("User already exists");
        }

        Proposal proposal = proposalRepository.findByEmail(email);
        if (proposal == null){
            throw new JwtAuthenticationException("Proposal does not exists");
        }

        User user = new User();
        user.setName(proposal.getName());
        user.setEmail(proposal.getEmail());
        user.setSurname(proposal.getSurname());
        user.setPhoneNumber(proposal.getPhoneNumber());
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRoles(proposal.getRoles());
        user.setIsActive(true);

        return userRepository.save(user);

    }
}
