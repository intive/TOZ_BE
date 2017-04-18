package com.intive.patronage.toz.tokens.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;

@ApiModel(value = "User Credentials")
@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserCredentialsView {

    @ApiModelProperty(example = "user@mail.com", position = 1, required = true)
    @NotNull
    @Email
    private final String email;

    @ApiModelProperty(example = "Password", position = 2, required = true)
    @NotNull
    private final String password;
}
