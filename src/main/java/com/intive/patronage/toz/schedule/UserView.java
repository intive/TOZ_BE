package com.intive.patronage.toz.schedule;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.intive.patronage.toz.model.db.IdentifiableView;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@ApiModel("User")
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class UserView extends IdentifiableView {

    private String name;
    private String lastName;
    private String login;
    private String email;
}
