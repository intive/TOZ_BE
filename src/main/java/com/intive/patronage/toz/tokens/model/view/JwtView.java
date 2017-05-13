package com.intive.patronage.toz.tokens.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.intive.patronage.toz.users.model.db.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

@ApiModel(value = "User info and JWT token")
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class JwtView {

    @ApiModelProperty(example = "c5296892-347f-4b2e-b1c6-6faff971f767", position = 1)
    private final UUID userId;
    @ApiModelProperty(example = "user@mail.com", position = 2)
    private final String email;
    @ApiModelProperty(position = 3)
    private final String name;
    @ApiModelProperty(position = 4)
    private final String surname;
    @ApiModelProperty(position = 5)
    private final Set<Role> roles;
    @ApiModelProperty(example = "1494750569", position = 6)
    private final Long expirationDate;
    @ApiModelProperty(position = 7)
    private final String jwt;
}
