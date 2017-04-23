package com.intive.patronage.toz.tokens;

import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.error.model.ErrorResponse;
import com.intive.patronage.toz.error.model.ValidationErrorResponse;
import com.intive.patronage.toz.tokens.model.UserContext;
import com.intive.patronage.toz.tokens.model.view.JwtView;
import com.intive.patronage.toz.tokens.model.view.UserCredentialsView;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = ApiUrl.TOKENS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
class TokensController {

    private final TokensService tokensService;

    @Autowired
    TokensController(TokensService tokensService) {
        this.tokensService = tokensService;
    }

    @ApiOperation("Login to api")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "JWT Token"),
            @ApiResponse(code = 400, message = "Validation error", response = ValidationErrorResponse.class),
            @ApiResponse(code = 401, message = "Incorrect user or password", response = ErrorResponse.class)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/acquire", consumes = MediaType.APPLICATION_JSON_VALUE)
    public JwtView login(@Valid @RequestBody UserCredentialsView credentials) {
        final Boolean isAuthenticated =
                tokensService.isUserAuthenticated(credentials.getEmail(), credentials.getPassword());

        if (!isAuthenticated) {
            throw new BadCredentialsException("Incorrect email or password");
        }

        return new JwtView(tokensService.getToken(credentials.getEmail()));
    }

    @Profile("dev")
    @ApiOperation(value = "Show current user", hidden = true)
    @PreAuthorize("hasAuthority('TOZ')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/whoami")
    public UserContext whoAmI(@AuthenticationPrincipal UserContext userContext) {
        return userContext;
    }
}
