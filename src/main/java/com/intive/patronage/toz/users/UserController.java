package com.intive.patronage.toz.users;

import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.error.model.ErrorResponse;
import com.intive.patronage.toz.error.model.ValidationErrorResponse;
import com.intive.patronage.toz.users.model.db.User;
import com.intive.patronage.toz.users.model.db.UserActivation;
import com.intive.patronage.toz.users.model.view.UserActivationView;
import com.intive.patronage.toz.users.model.view.UserView;
import com.intive.patronage.toz.util.ModelMapper;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;


@ApiIgnore
@Api(description = "Operations for users resource")
@RestController
@RequestMapping(value = ApiUrl.ADMIN_USERS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserActivationService userActivationService;

    @Autowired
    UserController(UserService userService, PasswordEncoder passwordEncoder, UserActivationService userActivationService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userActivationService = userActivationService;
    }

    @ApiOperation(value = "Get all users", responseContainer = "List", notes =
            "Required roles: SA, TOZ.")
    @GetMapping
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ')")
    public List<UserView> getAllUsers() {
        final List<User> users = userService.findAll();
        return ModelMapper.convertToView(users, UserView.class);
    }

    @ApiOperation(value = "Get user by id", notes =
            "Required roles: SA, TOZ.")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User not found", response = ErrorResponse.class),
    })
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ')")
    public UserView getUserById(@ApiParam(required = true) @PathVariable UUID id) {
        final User user = userService.findOneById(id);
        return convertToView(user);
    }

    private UserView convertToView(final User user) {
        return ModelMapper.convertToView(user, UserView.class);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create new user", response = UserView.class, notes =
            "Required roles: SA, TOZ.")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = ValidationErrorResponse.class)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ')")
    public ResponseEntity<UserView> createUser(@Valid @RequestBody UserView userView) {
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

    @ApiOperation(value = "Delete user", notes =
            "Required roles: SA, TOZ.")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User not found", response = ErrorResponse.class)
    })
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ')")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Update user information", response = UserView.class, notes =
            "Required roles: SA, TOZ.")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User not found", response = ErrorResponse.class),
            @ApiResponse(code = 400, message = "Bad request", response = ValidationErrorResponse.class)
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('SA', 'TOZ')")
    public UserView updateUser(@PathVariable UUID id, @Valid @RequestBody UserView userView) {
        final User user = convertToModel(userView);
        return convertToView(userService.update(id, user));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create new user using activation token", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = ValidationErrorResponse.class)
    })
    @PostMapping(value = "/activate_user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> activateUser(@Valid @RequestBody UserActivationView userActivationView) {

        User user = userActivationService.checkActivationToken(userActivationView.getToken(),userActivationView.getPassword());
        final URI baseLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        final String userLocationString = String.format("%s/%s", baseLocation, user.getId());
        final URI location = UriComponentsBuilder.fromUriString(userLocationString).build().toUri();
        return ResponseEntity.created(location).body(user);
    }
}
