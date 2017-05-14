package com.intive.patronage.toz.users.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.intive.patronage.toz.base.model.IdentifiableView;
import com.intive.patronage.toz.users.model.db.Role;
import com.intive.patronage.toz.util.validation.Phone;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@ApiModel(value = "User")
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserView extends IdentifiableView {

    @ApiModelProperty(example = "Johny", required = true, position = 1)
    @Size(min = 1, max = 35)
    @NotNull
    private String name;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty
    private String password;

    @ApiModelProperty(example = "Bravo", position = 2)
    @Size(min = 1, max = 35)
    @NotNull
    private String surname;

    @ApiModelProperty(example = "111222333", position = 3)
    @Phone
    private String phoneNumber;

    @ApiModelProperty(example = "johny.bravo@gmail.com", position = 4)
    @NotNull
    @Email
    private String email;

    @ApiModelProperty(example = "1490134074968", readOnly = true, position = 5)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Date passwordChangeDate;

    @NotEmpty
    private Set<Role> roles = new HashSet<>();

    public void setName(String name) {
        if (name != null) {
            this.name = name.trim();
        }
    }

    public void setSurname(String surname) {
        if (surname != null) {
            this.surname = surname.trim();
        }
    }

    public void setEmail(String email) {
        if (email != null) {
            this.email = email.toLowerCase();
        }
    }

    public void addRole(Role role) {
        roles.add(role);
    }
}
