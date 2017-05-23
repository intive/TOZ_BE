package com.intive.patronage.toz.users;

import com.intive.patronage.toz.error.exception.AlreadyExistsException;
import com.intive.patronage.toz.error.exception.BadRoleForSentUserBodyException;
import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.users.model.db.Role;
import com.intive.patronage.toz.users.model.db.User;
import com.intive.patronage.toz.util.RepositoryChecker;
import com.intive.patronage.toz.util.UserInfoGetter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private static final String USER = "User";
    private final UserRepository userRepository;

    @Autowired
    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findOneById(final UUID id) {
        RepositoryChecker.throwNotFoundExceptionIfNotExists(id, userRepository, USER);
        return userRepository.findOne(id);
    }

    public User findOneByEmail(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new NotFoundException(USER);
        }
        return userRepository.findByEmail(email);
    }

    public User findOneByName(String name) {
        if (!existsByName(name)) {
            throw new NotFoundException(USER);
        }
        return userRepository.findByName(name);
    }

    public boolean existsByName(String name) {
        return userRepository.existsByName(name);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    List<User> findAll() {
        return userRepository.findAll();
    }

    public User createWithPasswordHash(final User user, final String passwordHash) {
        throwBadRoleExceptionIfSentUserHasSuperAdminRole(user);
        final String email = user.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new AlreadyExistsException(USER);
        }
        user.setPasswordHash(passwordHash);
        return userRepository.save(user);
    }

    private void throwBadRoleExceptionIfSentUserHasSuperAdminRole(final User user) {
        if (UserInfoGetter.hasUserSuperAdminRole(user)) {
            throw new BadRoleForSentUserBodyException(Role.SA);
        }
    }

    public void delete(final UUID id) {
        RepositoryChecker.throwNotFoundExceptionIfNotExists(id, userRepository, USER);
        userRepository.delete(id);
    }

    public User update(final UUID id, final User user) {
        throwBadRoleExceptionIfSentUserHasSuperAdminRole(user);
        RepositoryChecker.throwNotFoundExceptionIfNotExists(id, userRepository, USER);
        user.setId(id);
        return userRepository.save(user);
    }
}
