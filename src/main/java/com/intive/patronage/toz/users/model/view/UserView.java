package com.intive.patronage.toz.users.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.intive.patronage.toz.base.model.IdentifiableView;
import com.intive.patronage.toz.users.model.enumerations.Role;
import com.intive.patronage.toz.util.validation.Phone;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;

@ApiModel(value = "User")
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserView extends IdentifiableView {

    @ApiModelProperty(example = "Johny", required = true, position = 1)
    @JsonProperty(required = true)
    @Size(max = 35)
    private String name;

    @ApiModelProperty(example = "Bravo", position = 2)
    @Size(max = 35)
    private String surname;

    @ApiModelProperty(example = "111 222 333", position = 3)
    @Phone
    @JsonProperty(required = true)
    private String phoneNumber;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password; // TODO

    @ApiModelProperty(example = "johny.bravo@gmail.com", position = 4)
    @Email
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;
}
