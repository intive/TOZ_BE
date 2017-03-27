package com.intive.patronage.toz.service;

import com.intive.patronage.toz.exception.NotFoundException;
import com.intive.patronage.toz.model.db.User;
import com.intive.patronage.toz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    private static final String USER = "User";

    private void throwNotFoundExceptionIfNotExists(final UUID id) {
        if (!userRepository.exists(id)) {
            throw new NotFoundException(USER);
        }
    }

    public User findOneById(UUID id){
        throwNotFoundExceptionIfNotExists(id);
        return userRepository.findOne(id);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }
    public User create(User user){
        return userRepository.save(user);
    }
    public void delete(UUID id){
        throwNotFoundExceptionIfNotExists(id);
        userRepository.delete(id);
    }
    public User update(final UUID id, final User user) {
        throwNotFoundExceptionIfNotExists(id);
        User editUser = userRepository.findOne(user.getId());
        editUser.setForename(user.getForename());
        editUser.setSurname(user.getSurname());
        editUser.setRole(user.getRole());
        return userRepository.save(editUser);

    }

}
