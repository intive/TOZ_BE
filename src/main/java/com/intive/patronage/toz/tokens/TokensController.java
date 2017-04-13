package com.intive.patronage.toz.tokens;

import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.tokens.model.UserContext;
import com.intive.patronage.toz.users.model.view.UserView;
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

import javax.validation.Valid;

@RestController
@RequestMapping(value = ApiUrl.TOKENS_PATH)
class TokensController {

    private final TokensService tokensService;
    private final JwtFactory jwtFactory;

    @Autowired
    public TokensController(TokensService tokensService, JwtFactory jwtFactory) {
        this.tokensService = tokensService;
        this.jwtFactory = jwtFactory;
    }

    @ApiOperation("Login to api")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Logged in"),
            @ApiResponse(code = 401, message = "Incorrect user or password")
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = ApiUrl.ACQUIRE_TOKEN_PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String login(@Valid @RequestBody UserView userView) {
        final Boolean isAuthenticated = tokensService.isUserAuthenticated(userView.getPassword(), userView.getEmail());
        if (!isAuthenticated) {
            throw new BadCredentialsException("Incorrect email or password");
        }

        return jwtFactory.generateToken(tokensService.getUserByMail(userView.getEmail()));
    }

    @ApiOperation(value = "Show current user content", hidden = true)
    @PreAuthorize("hasAuthority('TOZ')")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @GetMapping(value = "/whoami")
    public UserContext whoAmI(@AuthenticationPrincipal UserContext userContext) {
        return userContext;
    }
}
