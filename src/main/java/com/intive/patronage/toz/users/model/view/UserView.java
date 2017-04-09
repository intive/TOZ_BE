package com.intive.patronage.toz.users.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.intive.patronage.toz.base.model.IdentifiableView;
import com.intive.patronage.toz.users.model.db.User;
import com.intive.patronage.toz.util.validation.Phone;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel(value = "User")
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserView extends IdentifiableView {

    @ApiModelProperty(example = "Johny", required = true, position = 1)
    @Size(min = 3, max = 35)
    @NotNull
    private String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password; // TODO

    @ApiModelProperty(example = "Bravo", position = 2)
    @Size(min = 3, max = 35)
    @NotNull
    private String surname;

    @ApiModelProperty(example = "111222333", position = 3)
    @Phone
    private String phoneNumber;

    @ApiModelProperty(example = "johny.bravo@gmail.com", position = 4)
    @NotNull
    @Email
    private String email;

    @NotNull
    private User.Role role;
}
