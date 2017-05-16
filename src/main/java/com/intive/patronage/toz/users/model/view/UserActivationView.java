package com.intive.patronage.toz.users.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;

@ApiModel(value = "UserActivation")
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserActivationView {

    @ApiModelProperty(example = "johny.bravo@gmail.com")
    @NotNull
    @Email
    private String email;

    @ApiModelProperty(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")
    @NotNull
    private String token;

    @ApiModelProperty(example = "12345")
    @NotNull
    private String password;
}
