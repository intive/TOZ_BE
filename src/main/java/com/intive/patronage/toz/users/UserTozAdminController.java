package com.intive.patronage.toz.users;

import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.error.exception.BadRoleForExistingUserException;
import com.intive.patronage.toz.error.exception.BadRoleForNewUserException;
import com.intive.patronage.toz.error.model.ErrorResponse;
import com.intive.patronage.toz.error.model.ValidationErrorResponse;
import com.intive.patronage.toz.users.model.db.User;
import com.intive.patronage.toz.users.model.view.UserView;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Api(tags = "Users", description = "Operations for users resource")
@PreAuthorize("hasAuthority('TOZ')")
@RestController
@RequestMapping(value = ApiUrl.USERS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
class UserTozAdminController extends UserBasicController {

    private static final String API_METHOD_NOTES = "Required TOZ role";

    @Autowired
    UserTozAdminController(UserService userService, PasswordEncoder passwordEncoder) {
        super(userService, passwordEncoder);
    }

    @ApiOperation(value = "Get all users", notes = API_METHOD_NOTES)
    @GetMapping
    @Override
    public List<UserView> getAllUsers() {
        return super.getAllUsers();
    }

    @ApiOperation(value = "Get user by id", notes = API_METHOD_NOTES)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User not found", response = ErrorResponse.class),
    })
    @GetMapping(value = "/{id}")
    @Override
    public UserView getUserById(@ApiParam(required = true) @PathVariable UUID id) {
        return super.getUserById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create new user", response = UserView.class, notes = API_METHOD_NOTES)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = ValidationErrorResponse.class)
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<UserView> createUser(@Valid @RequestBody UserView userView) {
        return super.createUser(userView);
    }

    @ApiOperation(value = "Delete user", notes = API_METHOD_NOTES)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User not found", response = ErrorResponse.class)
    })
    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        return super.deleteUser(id);
    }

    @ApiOperation(value = "Update user information", response = UserView.class, notes = API_METHOD_NOTES)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User not found", response = ErrorResponse.class),
            @ApiResponse(code = 400, message = "Bad request", response = ValidationErrorResponse.class)
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Override
    public UserView updateUser(@PathVariable UUID id, @Valid @RequestBody UserView userView) {
        return super.updateUser(id, userView);
    }
}
