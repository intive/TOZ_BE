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
public class UsersService {

    private static final String USER = "User";
    private final UsersRepository usersRepository;

    @Autowired
    UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    User findOneById(final UUID id) {
        throwNotFoundExceptionIfIdNotExists(id);
        return usersRepository.findOne(id);
    }

    private void throwNotFoundExceptionIfIdNotExists(final UUID id) {
        if (!usersRepository.exists(id)) {
            throw new NotFoundException(USER);
        }
    }

    public User findOneByEmail(String email) {
        if (!usersRepository.existsByEmail(email)) {
            throw new NotFoundException(USER);
        }
        return usersRepository.findByEmail(email);
    }

    public User findOneByName(String name) {
        if (!existsByName(name)) {
            throw new NotFoundException(USER);
        }
        return usersRepository.findByName(name);
    }

    public boolean existsByName(String name) {
        return usersRepository.existsByName(name);
    }

    List<User> findAll() {
        return usersRepository.findAll();
    }

    public User createWithPasswordHash(final User user, final String passwordHash) {
        final String email = user.getEmail();
        if (usersRepository.existsByEmail(email)) {
            throw new AlreadyExistsException(USER);
        }
        user.setPasswordHash(passwordHash);
        return usersRepository.save(user);
    }

    public void delete(final UUID id) {
        throwNotFoundExceptionIfIdNotExists(id);
        usersRepository.delete(id);
    }

    User update(final UUID id, final User user) {
        throwNotFoundExceptionIfIdNotExists(id);
        user.setId(id);
        return usersRepository.save(user);
    }
}
