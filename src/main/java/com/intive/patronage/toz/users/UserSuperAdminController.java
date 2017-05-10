package com.intive.patronage.toz.users;

import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.users.model.view.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@ApiIgnore
@RestController
@RequestMapping(value = ApiUrl.SUPER_ADMIN_USERS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
class UserSuperAdminController extends UserBasicController {

    @Autowired
    UserSuperAdminController(UserService userService, PasswordEncoder passwordEncoder) {
        super(userService, passwordEncoder);
    }

    @GetMapping
    @Override
    public List<UserView> getAllUsers() {
        return super.getAllUsers();
    }

    @GetMapping(value = "/{id}")
    @Override
    public UserView getUserById(@PathVariable UUID id) {
        return super.getUserById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    protected ResponseEntity<UserView> createUser(@Valid @RequestBody UserView userView) {
        return super.createUser(userView);
    }

    @DeleteMapping(value = "/{id}")
    @Override
    protected ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        return super.deleteUser(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    protected UserView updateUser(@PathVariable UUID id, @Valid @RequestBody UserView userView) {
        return super.updateUser(id, userView);
    }
}
