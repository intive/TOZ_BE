package com.intive.patronage.toz.howtohelp.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@ApiModel("How to help information")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class HelpInfoView {

    @NotEmpty
    @ApiModelProperty(value = "Description on how to help.", required = true, example = "If you want to help then..")
    private String howToHelpDescription;

    @ApiModelProperty(example = "12411688844654")
    private Long modificationDate;

    public HelpInfoView(String howToHelpDescription, Long modificationDate) {
        this.howToHelpDescription = howToHelpDescription;
        this.modificationDate = modificationDate;
    }
}
