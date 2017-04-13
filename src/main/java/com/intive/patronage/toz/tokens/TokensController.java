package com.intive.patronage.toz.tokens;

import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.users.model.view.UserView;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = ApiUrl.TOKENS_PATH)
class TokensController {

    private final TokensService tokensService;
    private final JwtService jwtService;

    @Autowired
    public TokensController(TokensService tokensService, JwtService jwtService) {
        this.tokensService = tokensService;
        this.jwtService = jwtService;
    }

    @ApiOperation("Login to api")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Logged in"),
            @ApiResponse(code = 401, message = "Incorrect user or password")
    })
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/acquire", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String login(@Valid @RequestBody UserView userView) {
        Boolean isAuthenticated = tokensService.isUserAuthenticated(userView.getPassword(), userView.getEmail());
        if (!isAuthenticated) {
            throw new BadCredentialsException("Incorrect email or password");
        }

        return jwtService.generateToken(tokensService.getUserByMail(userView.getEmail()));
    }
}
