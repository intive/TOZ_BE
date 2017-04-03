package com.intive.patronage.toz.users;

import com.intive.patronage.toz.error.model.ErrorResponse;
import com.intive.patronage.toz.error.model.ValidationErrorResponse;
import com.intive.patronage.toz.users.model.db.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;


@Api(description = "Operations for users resource")
@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsersController {


    private final UserService userService;

    @Autowired
    UsersController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "Get all users", responseContainer = "List")
    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @ApiOperation(value = "Get user by id")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User not found", response = ErrorResponse.class),
    })
    @GetMapping(value = "/{id}")
    public User getUserById(@ApiParam(required = true) @PathVariable UUID id) {
        return userService.findOneById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create new user", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = ValidationErrorResponse.class)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User createdUser = userService.create(user);
        final URI baseLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        final String userLocationString = String.format("%s%s", baseLocation, createdUser.getId());
        final URI location = UriComponentsBuilder.fromUriString(userLocationString).build().toUri();
        return ResponseEntity.created(location).body(createdUser);
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

    @ApiOperation(value = "Update user information", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User not found", response = ErrorResponse.class),
            @ApiResponse(code = 400, message = "Bad request", response = ValidationErrorResponse.class)
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public User updateUser(@PathVariable UUID id, @Valid @RequestBody User user) {
        return userService.update(id, user);
    }

}
