package com.intive.patronage.toz.users;

import com.intive.patronage.toz.error.exception.AlreadyExistsException;
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
        throwNotFoundExceptionIfIdNotExists(id);
        return userRepository.findOne(id);
    }

    private void throwNotFoundExceptionIfIdNotExists(final UUID id) {
        if (!userRepository.exists(id)) {
            throw new NotFoundException(USER);
        }
    }

    public User findOneByEmail(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new NotFoundException(USER);
        }
        return userRepository.findByEmail(email);
    }

    public User findOneByName(String name) {
        if (!userRepository.existsByName(name)) {
            throw new NotFoundException(USER);
        }
        return userRepository.findByName(name);
    }

    List<User> findAll() {
        return userRepository.findAll();
    }

    User createWithPassword(final User user, final String password) {
        final String email = user.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new AlreadyExistsException(USER);
        }
        final String passwordHash = passwordEncoder.encode(password);
        user.setPasswordHash(passwordHash);
        return userRepository.save(user);
    }

    void delete(final UUID id) {
        throwNotFoundExceptionIfIdNotExists(id);
        userRepository.delete(id);
    }

    User update(final UUID id, final User user) {
        throwNotFoundExceptionIfIdNotExists(id);
        user.setId(id);
        return userRepository.save(user);
    }
}
