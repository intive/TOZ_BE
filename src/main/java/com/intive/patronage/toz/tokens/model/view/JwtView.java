package com.intive.patronage.toz.tokens.model.view;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@ApiModel(value = "JWT Token")
@Getter
@AllArgsConstructor
public class JwtView {

    private final String jwt;
}
