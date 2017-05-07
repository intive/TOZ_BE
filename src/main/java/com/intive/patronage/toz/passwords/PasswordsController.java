package com.intive.patronage.toz.passwords;

import com.intive.patronage.toz.error.model.ErrorResponse;
import com.intive.patronage.toz.passwords.model.PasswordChangeRequestView;
import com.intive.patronage.toz.passwords.model.PasswordResponseView;
import com.intive.patronage.toz.tokens.model.UserContext;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

import static com.intive.patronage.toz.config.ApiUrl.PASSWORDS_PATH;

@RestController
@RequestMapping(value = PASSWORDS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class PasswordsController {

    private final PasswordsService passwordsService;
    private final MessageSource messageSource;

    @Autowired
    PasswordsController(PasswordsService passwordsService, MessageSource messageSource) {
        this.passwordsService = passwordsService;
        this.messageSource = messageSource;
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

        passwordsService.changePassword(userContext.getEmail(),
                passwordChangeRequest.getOldPassword(),
                passwordChangeRequest.getNewPassword());

        final String message = messageSource.getMessage(
                "passwordChangeSuccessful", null, LocaleContextHolder.getLocale());

        return new PasswordResponseView(message);
    }
}
