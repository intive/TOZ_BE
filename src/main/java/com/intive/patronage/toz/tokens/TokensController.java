package com.intive.patronage.toz.tokens;

import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.users.model.view.UserCredentials;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            @ApiResponse(code = 200, message = "Logged in"),
            @ApiResponse(code = 403, message = "Incorrect user or password")
    })
    @PostMapping(value = "/acquire", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@Valid @RequestBody UserCredentials userCredentials) {
        final String email = userCredentials.getEmail();
        final String password = userCredentials.getPassword();
        Boolean isLoggedIn = tokensService.isUserAuthenticated(email, password);
        HttpStatus httpStatus = isLoggedIn ? HttpStatus.OK : HttpStatus.FORBIDDEN;
        return ResponseEntity
                .status(httpStatus)
                .body(null); //TODO - create and return to client JWT token
    }

}
