package com.intive.patronage.toz.proposals.model;

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
import java.util.HashSet;
import java.util.Set;

@ApiModel(value = "Proposal")
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProposalView extends IdentifiableView {
    @ApiModelProperty(example = "Johny", required = true, position = 1)
    @Size(min = 1, max = 35)
    @NotNull
    private String name;

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

    @NotEmpty
    private Set<Role> roles = new HashSet<>();

    @ApiModelProperty(example = "1490134074968")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long creationDate;

    @ApiModelProperty(example = "false")
    private boolean isRead;
}
