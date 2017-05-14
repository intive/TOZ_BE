package com.intive.patronage.toz.users;

import com.intive.patronage.toz.users.model.db.User;
import com.intive.patronage.toz.users.model.view.UserView;
import com.intive.patronage.toz.util.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

abstract class UserBasicController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    UserBasicController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    protected List<UserView> getAllUsers() {
        final List<User> users = userService.findAll();
        return ModelMapper.convertToView(users, UserView.class);
    }

    protected UserView getUserById(UUID id) {
        final User user = userService.findOneById(id);
        return convertToView(user);
    }

    private UserView convertToView(final User user) {
        return ModelMapper.convertToView(user, UserView.class);
    }

    protected ResponseEntity<UserView> createUser(UserView userView) {
        final String passwordHash = passwordEncoder.encode(userView.getPassword());
        final User createdUser = userService.createWithPasswordHash(convertToModel(userView), passwordHash);
        final UserView createdUserView = convertToView(createdUser);
        final URI baseLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        final String userLocationString = String.format("%s/%s", baseLocation, createdUser.getId());
        final URI location = UriComponentsBuilder.fromUriString(userLocationString).build().toUri();
        return ResponseEntity.created(location).body(createdUserView);
    }

    private User convertToModel(final UserView userView) {
        return ModelMapper.convertToModel(userView, User.class);
    }

    protected ResponseEntity<?> deleteUser(UUID id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

    protected UserView updateUser(UUID id, UserView userView) {
        final User user = convertToModel(userView);
        return convertToView(userService.update(id, user));
    }

}
