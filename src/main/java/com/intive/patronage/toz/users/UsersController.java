package com.intive.patronage.toz.users;

import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.error.model.ErrorResponse;
import com.intive.patronage.toz.error.model.ValidationErrorResponse;
import com.intive.patronage.toz.users.model.db.User;
import com.intive.patronage.toz.users.model.view.UserView;
import com.intive.patronage.toz.util.ModelMapper;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;


@Api(description = "Operations for users resource")
@RestController
@RequestMapping(value = ApiUrl.ADMIN_USERS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class UsersController {

    private final UserService userService;

    @Autowired
    UsersController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Get all users", responseContainer = "List")
    @GetMapping
    public List<UserView> getAllUsers() {
        final List<User> users = userService.findAll();
        return ModelMapper.convertToView(users, UserView.class);
    }

    @ApiOperation(value = "Get user by id")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User not found", response = ErrorResponse.class),
    })
    @GetMapping(value = "/{id}")
    public UserView getUserById(@ApiParam(required = true) @PathVariable UUID id) {
        final User user = userService.findOneById(id);
        return convertToView(user);
    }

    private UserView convertToView(final User user) {
        return ModelMapper.convertToView(user, UserView.class);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create new user", response = UserView.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = ValidationErrorResponse.class)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserView> createUser(@Valid @RequestBody UserView userView) {
        final User createdUser = userService.createWithPassword(convertToModel(userView), userView.getPassword());
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

    @ApiOperation("Delete user")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User not found", response = ErrorResponse.class)
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Update user information", response = UserView.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User not found", response = ErrorResponse.class),
            @ApiResponse(code = 400, message = "Bad request", response = ValidationErrorResponse.class)
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserView updateUser(@PathVariable UUID id, @Valid @RequestBody UserView userView) {
        final User user = convertToModel(userView);
        return convertToView(userService.update(id, user));
    }

}
