package com.intive.patronage.toz.tokens.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intive.patronage.toz.error.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

class TokenAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        MAPPER.writeValue(response.getWriter(), new ErrorResponse(HttpStatus.UNAUTHORIZED, exception.getMessage()));
    }
}
