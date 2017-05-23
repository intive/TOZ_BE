package com.intive.patronage.toz.passwords.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;


@Getter
@Setter
@NoArgsConstructor
public class PasswordRequestViewBase {

    @ApiModelProperty(value = "New password", required = true, position = 2)
    @NotEmpty
    protected String newPassword;

}
