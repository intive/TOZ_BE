package com.intive.patronage.toz.users;

import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.users.model.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {


    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static final String USER = "User";

    private void throwNotFoundExceptionIfNotExists(final UUID id) {
        if (!userRepository.exists(id)) {
            throw new NotFoundException(USER);
        }
    }

    public User findOneById(UUID id) {
        throwNotFoundExceptionIfNotExists(id);
        return userRepository.findOne(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User create(User user) {
        return userRepository.save(user);
    }

    public void delete(UUID id) {
        throwNotFoundExceptionIfNotExists(id);
        userRepository.delete(id);
    }

    public User update(final UUID id, final User user) {
        throwNotFoundExceptionIfNotExists(id);
        user.setId(id);
        return userRepository.save(user);
    }
}
