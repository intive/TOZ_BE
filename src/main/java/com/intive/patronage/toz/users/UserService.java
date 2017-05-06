package com.intive.patronage.toz.users;

import com.intive.patronage.toz.error.exception.AlreadyExistsException;
import com.intive.patronage.toz.error.exception.BadRoleForExistingUserException;
import com.intive.patronage.toz.error.exception.BadRoleForNewUserException;
import com.intive.patronage.toz.error.exception.NotFoundException;
import com.intive.patronage.toz.users.model.db.RoleEntity;
import com.intive.patronage.toz.users.model.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {

    private static final String USER = "User";
    private final UserRepository userRepository;

    @Autowired
    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        if (!existsByName(name)) {
            throw new NotFoundException(USER);
        }
        return userRepository.findByName(name);
    }

    public boolean existsByName(String name) {
        return userRepository.existsByName(name);
    }

    List<User> findAll() {
        return userRepository.findAll();
    }

    public User createWithPasswordHash(final User user, final String passwordHash) {
        if (isUserHasSuperAdminRole(user)) {
            throw new BadRoleForNewUserException(User.Role.SA);
        }
        final String email = user.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new AlreadyExistsException(USER);
        }
        user.setPasswordHash(passwordHash);
        return userRepository.save(user);
    }

    public void delete(final UUID id) {
        throwNotFoundExceptionIfIdNotExists(id);
        userRepository.delete(id);
    }

    User update(final UUID id, final User user) {
        if (isUserHasSuperAdminRole(user)) {
            throw new BadRoleForExistingUserException(User.Role.SA);
        }
        throwNotFoundExceptionIfIdNotExists(id);
        user.setId(id);
        return userRepository.save(user);
    }

    private boolean isUserHasSuperAdminRole(final User user) { // TODO
        Set<RoleEntity> userRoles = user.getRoles();
        for (RoleEntity roleEntity : userRoles) {
            User.Role userRole = roleEntity.getRole();
            if (userRole.equals(User.Role.SA)) {
                return true;
            }
        }
        return false;
    }
}
