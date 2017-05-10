package com.intive.patronage.toz.tokens.model.view;

import com.intive.patronage.toz.users.model.db.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@ApiModel(value = "User info and JWT token")
@Getter
@Builder
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
    private final List<User.Role> roles;
    @ApiModelProperty(position = 6)
    private final String jwt;
}
