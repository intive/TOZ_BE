package com.intive.patronage.toz.tokens;

import com.intive.patronage.toz.config.ApiUrl;
import com.intive.patronage.toz.users.model.view.UserView;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = ApiUrl.TOKENS_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
class TokensController {

    private final TokensService tokensService;

    @Autowired
    public TokensController(TokensService tokensService) {
        this.tokensService = tokensService;
    }


    @ApiOperation("Login to api")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Logged in"),
            @ApiResponse(code = 422, message = "Incorrect user or password")
    })
    @PostMapping(value = "/acquire", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> login(@Valid @RequestBody UserView userView) {
        final boolean login = tokensService.login(userView);
        if (login) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(null);
        } else {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(null);
        }
    }

}
