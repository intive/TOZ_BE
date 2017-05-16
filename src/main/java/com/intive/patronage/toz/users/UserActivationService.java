package com.intive.patronage.toz.users;


import com.intive.patronage.toz.error.exception.JwtAuthenticationException;
import com.intive.patronage.toz.mail.MailService;
import com.intive.patronage.toz.mail.MailTemplatesService;
import com.intive.patronage.toz.proposals.ProposalRepository;
import com.intive.patronage.toz.proposals.model.Proposal;
import com.intive.patronage.toz.tokens.*;
import com.intive.patronage.toz.tokens.JwtParser;
import com.intive.patronage.toz.users.model.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;


@Service
public class UserActivationService {

    private final String USER_ALREADY_EXISTS = "User already exists";
    private final String PROPOSAL_DOES_NOT_EXISTS = "Proposal does not exists";

    @Value("${jwt.email.activation.expiration-time-minutes}")
    private long expirationTime;
    @Value("${jwt.secret-base64}")
    private String secret;
    @Value("${mail.register.subject}")
    private String mailSubject;

    private JwtFactory jwtFactory;
    private UserRepository userRepository;
    private ProposalRepository proposalRepository;
    private MailTemplatesService mailTemplatesService;
    private MailService mailService;
    private PasswordEncoder passwordEncoder;
    private JwtParser jwtParser;

    @Autowired
    public UserActivationService(
            JwtFactory jwtFactory,
            UserRepository userRepository,
            ProposalRepository proposalRepository,
            JwtParser jwtParser,
            MailService mailService,
            MailTemplatesService mailTemplatesService,
            PasswordEncoder passwordEncoder
        ) {
        this.mailService = mailService;
        this.jwtParser = jwtParser;
        this.jwtFactory = jwtFactory;
        this.userRepository = userRepository;
        this.proposalRepository = proposalRepository;
        this.mailTemplatesService = mailTemplatesService;
        this.passwordEncoder = passwordEncoder;
    }

    public void sendActivationEmail(Proposal proposal) throws IOException, MessagingException {

        System.out.println(proposal);
        String token = jwtFactory.generateToken(proposal, expirationTime);
        String mailBody = mailTemplatesService.getRegistrationTemplate(token);
        mailService.sendMail(mailSubject, mailBody, proposal.getEmail());
    }

    public User checkActivationToken(String token, String password) {
        jwtParser.parse(token);

        String email = jwtParser.getEmail();

        if (userRepository.findByEmail(email) != null){
            throw new JwtAuthenticationException(USER_ALREADY_EXISTS);
        }

        Proposal proposal = proposalRepository.findByEmail(email);
        if (proposal == null){
            throw new JwtAuthenticationException(PROPOSAL_DOES_NOT_EXISTS);
        }

        User user = new User();
        user.setName(proposal.getName());
        user.setEmail(proposal.getEmail());
        user.setSurname(proposal.getSurname());
        user.setPhoneNumber(proposal.getPhoneNumber());
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setRoles(proposal.getRoles());
        user.setActive(true);

        return userRepository.save(user);

    }
}
