package com.intive.patronage.toz.status.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.intive.patronage.toz.base.model.IdentifiableView;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel(value = "Pets status")
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PetStatusView extends IdentifiableView{

    @ApiModelProperty(example = "DO ADOPCJI", required = true, position = 1)
    @Size(min = 1, max = 35)
    @NotNull
    private String name;

    @ApiModelProperty(example = "#0000FF", required = true, position = 2)
    @Size(min = 2, max = 7)
    @NotNull
    private String rgb;

    @ApiModelProperty(example = "false")
    private boolean isPublic = false;
}
