package com.intive.patronage.toz.passwords;

import com.intive.patronage.toz.error.model.ErrorResponse;
import com.intive.patronage.toz.error.model.ValidationErrorResponse;
import com.intive.patronage.toz.passwords.model.PasswordChangeRequestView;
import com.intive.patronage.toz.passwords.model.PasswordRequestSendTokenView;
import com.intive.patronage.toz.passwords.model.PasswordResetRequestView;
import com.intive.patronage.toz.passwords.model.PasswordResponseView;
import com.intive.patronage.toz.tokens.model.UserContext;
import com.intive.patronage.toz.users.UserService;
import com.intive.patronage.toz.users.model.db.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import javax.mail.MessagingException;
import javax.validation.Valid;

import java.io.IOException;
import java.net.URI;

import static com.intive.patronage.toz.config.ApiUrl.PASSWORDS_PATH;
import static com.intive.patronage.toz.config.ApiUrl.PASSWORDS_RESET_PATH;
import static com.intive.patronage.toz.config.ApiUrl.PASSWORDS_RESET_SEND_TOKEN_PATH;

@Api(tags = "Passwords", description = "Passwords operations (change and reset).")
@RestController
@RequestMapping(value = PASSWORDS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class PasswordsController {

    private final PasswordsService passwordsService;
    private final MessageSource messageSource;
    private final UserService userService;
    private final PasswordsResetService passwordsResetService;

    @Autowired
    PasswordsController(PasswordsService passwordsService, MessageSource messageSource, UserService userService, PasswordsResetService passwordsResetService) {
        this.passwordsService = passwordsService;
        this.messageSource = messageSource;
        this.userService = userService;
        this.passwordsResetService = passwordsResetService;
    }

    @ApiOperation(value = "Change Password", notes = "Required roles: TOZ, VOLUNTEER")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Password changed"),
            @ApiResponse(code = 400, message = "Wrong password", response = ErrorResponse.class)
    })
    @PreAuthorize("hasAnyAuthority('TOZ', 'VOLUNTEER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public PasswordResponseView changePassword(@Valid @RequestBody PasswordChangeRequestView passwordChangeRequest,
                                               @ApiIgnore @AuthenticationPrincipal UserContext userContext) {

        passwordsService.changePasswordForExistingUser(userContext.getEmail(),
                passwordChangeRequest.getOldPassword(),
                passwordChangeRequest.getNewPassword());

        final String message = messageSource.getMessage(
                "passwordChangeSuccessful", null, LocaleContextHolder.getLocale());

        return new PasswordResponseView(message);
    }
    @ApiOperation(value = "Send email with reset password token")
    @PostMapping(value = PASSWORDS_RESET_SEND_TOKEN_PATH )
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "User not found", response = ErrorResponse.class),
    })
    public User sendResetPasswordEmail(@PathVariable PasswordRequestSendTokenView passwordRequestSendTokenView) throws IOException, MessagingException {
        User user = userService.findOneByEmail(passwordRequestSendTokenView.getEmail());
        passwordsResetService.sendResetPaswordToken(user);
        return user;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Reset password using activation token", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Bad request", response = ValidationErrorResponse.class)
    })
    @PostMapping(value = PASSWORDS_RESET_PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> resetPassword(@Valid @RequestBody PasswordResetRequestView passwordResetRequestView) {

        User user = passwordsResetService.changePasswordUsingToken(passwordResetRequestView.getToken(),passwordResetRequestView.getNewPassword());

        final URI baseLocation = ServletUriComponentsBuilder.fromCurrentRequest()
                .build().toUri();
        final String userLocationString = String.format("%s/%s", baseLocation, user.getId());
        final URI location = UriComponentsBuilder.fromUriString(userLocationString).build().toUri();
        return ResponseEntity.created(location).body(user);
    }
}
