package com.intive.patronage.toz.passwords.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

@ApiModel("Password send reset token request body")
@Getter
@Setter
@NoArgsConstructor
public class PasswordRequestSendTokenView {

    @ApiModelProperty(value = "Email", required = true, position = 1)
    @NotEmpty
    private String email;

    public PasswordRequestSendTokenView(String email) {
        this.email = email;
    }
}
