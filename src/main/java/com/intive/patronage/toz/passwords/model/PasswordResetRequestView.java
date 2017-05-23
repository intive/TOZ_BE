package com.intive.patronage.toz.passwords.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

@ApiModel("Password reset request body")
@Getter
@Setter
@NoArgsConstructor
public class PasswordResetRequestView extends PasswordRequestViewBase {

    @ApiModelProperty(value = "Reset Token", required = true, position = 1)
    @NotEmpty
    private String token;

    public PasswordResetRequestView(String newPassword) {
        this.newPassword = newPassword;
    }
}
