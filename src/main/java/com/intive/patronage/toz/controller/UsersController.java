package com.intive.patronage.toz.controller;

import com.intive.patronage.toz.error.ErrorResponse;
import com.intive.patronage.toz.error.ValidationErrorResponse;
import com.intive.patronage.toz.model.db.User;
import com.intive.patronage.toz.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;


@Api(description = "Operations for users resource")
@RestController
@RequestMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsersController {


    private final UserService userService;

    @Autowired
    UsersController(UserService userService){
        this.userService = userService;
    }

    @ApiOperation(value = "Get all pets", responseContainer = "List")
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

    @ApiOperation(value = "Create new pet", response = User.class)
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
            @ApiResponse(code = 404, message = "Pet not found", response = ErrorResponse.class),
            @ApiResponse(code = 400, message = "Bad request", response = ValidationErrorResponse.class)
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public User updateUser(@PathVariable UUID id, @Valid @RequestBody User user) {
        return userService.update(id, user);
    }

}
