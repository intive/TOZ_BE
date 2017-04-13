package com.intive.patronage.toz.users;

import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.users.model.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private static final String USER = "User";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    User findOneById(final UUID id) {
        throwNotFoundExceptionIfNotExists(id);
        return userRepository.findOne(id);
    }

    private void throwNotFoundExceptionIfNotExists(final UUID id) {
        if (!userRepository.exists(id)) {
            throw new NotFoundException(USER);
        }
    }

    public User findOneByEmail(String email) {
        throwNotFoundExceptionIfNotExists(email);
        return userRepository.findByEmail(email);
    }

    private void throwNotFoundExceptionIfNotExists(final String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new NotFoundException(USER);
        }
    }

    List<User> findAll() {
        return userRepository.findAll();
    }

    User createWithPassword(final User user, final String password) {
        final String passwordHash = passwordEncoder.encode(password);
        user.setPasswordHash(passwordHash);
        return userRepository.save(user);
    }

    void delete(final UUID id) {
        throwNotFoundExceptionIfNotExists(id);
        userRepository.delete(id);
    }

    User update(final UUID id, final User user) {
        throwNotFoundExceptionIfNotExists(id);
        user.setId(id);
        return userRepository.save(user);
    }
}
