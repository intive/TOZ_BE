package com.intive.patronage.toz.users.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.intive.patronage.toz.base.model.IdentifiableView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;

@ApiModel(value = "User")
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserView extends IdentifiableView {

    @ApiModelProperty(example = "jan@poczta.pl", position = 1, required = true)
    @NotNull
    @Email
    private String email;

    @ApiModelProperty(example = "P@ssword1", position = 2, required = true)
    @NotNull
    private String password;
}
