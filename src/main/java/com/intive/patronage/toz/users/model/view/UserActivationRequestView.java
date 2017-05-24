package com.intive.patronage.toz.users.model.view;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@ApiModel("Send Activation token request body")
@Getter
@Setter
@NoArgsConstructor
public class UserActivationRequestView {

    @ApiModelProperty(value = "Proposal uuid", required = true, position = 1)
    @NotNull
    private UUID uuid;
}
