package com.intive.patronage.toz.tokens;

import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.error.model.ErrorResponse;
import com.intive.patronage.toz.error.model.ValidationErrorResponse;
import com.intive.patronage.toz.tokens.model.UserContext;
import com.intive.patronage.toz.tokens.model.view.JwtView;
import com.intive.patronage.toz.tokens.model.view.UserCredentialsView;
import com.intive.patronage.toz.users.UserService;
import com.intive.patronage.toz.users.model.db.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Api(tags = "Tokens")
@RestController
@RequestMapping(value = ApiUrl.TOKENS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
class TokensController {

    private final TokensService tokensService;
    private final UserService userService;
    private final JwtParser jwtParser;

    @Autowired
    TokensController(TokensService tokensService, UserService userService, JwtParser jwtParser) {
        this.tokensService = tokensService;
        this.userService = userService;
        this.jwtParser = jwtParser;
    }

    @ApiOperation("Login to api")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User info and JWT token"),
            @ApiResponse(code = 400, message = "Validation error", response = ValidationErrorResponse.class),
            @ApiResponse(code = 401, message = "Incorrect user or password", response = ErrorResponse.class)
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/acquire", consumes = MediaType.APPLICATION_JSON_VALUE)
    public JwtView login(@Valid @RequestBody UserCredentialsView credentials) {
        final User user = userService.findOneByEmail(credentials.getEmail());

        final Boolean isAuthenticated = tokensService.isUserAuthenticated(user, credentials.getPassword());
        if (!isAuthenticated) {
            throw new BadCredentialsException("Incorrect email or password");
        }

        final String token = tokensService.getToken(user);
        jwtParser.parse(token);

        return JwtView.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .surname(user.getSurname())
                .roles(user.getRoles())
                .expirationDate(jwtParser.getTokenExpirationDate().toInstant().getEpochSecond())
                .jwt(token)
                .build();
    }

    @ApiOperation(value = "Show current user", notes = "Required roles: TOZ, VOLUNTEER")
    @PreAuthorize("hasAnyAuthority('TOZ', 'VOLUNTEER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/whoami")
    public UserContext whoAmI(@ApiIgnore @AuthenticationPrincipal UserContext userContext) {
        return userContext;
    }
}
