package com.intive.patronage.toz.status.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.intive.patronage.toz.base.model.IdentifiableView;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@ApiModel(value = "Pets status")
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PetsStatusView extends IdentifiableView{

    @NotNull
    private String name;

    @NotNull
    private String rgb;

    private boolean isPublic = false;
}
