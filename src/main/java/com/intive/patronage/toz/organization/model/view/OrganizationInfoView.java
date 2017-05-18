package com.intive.patronage.toz.organization.model.view;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

@ApiModel("Organization information")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
@Builder
@JsonDeserialize(builder = OrganizationInfoView.OrganizationInfoViewBuilder.class)
public class OrganizationInfoView {

    @NotEmpty
    @ApiModelProperty(value = "Organization name", required = true, example = "TOZ Szczecin", position = 1)
    private final String name;
    @Valid
    @ApiModelProperty(position = 2)
    private final AddressView address;
    @Valid
    @ApiModelProperty(position = 3)
    private final ContactView contact;
    @Valid
    @ApiModelProperty(position = 4)
    private final BankAccountView bankAccount;
    @ApiModelProperty(value = "Invitation text", example = "Hello to TOZ", position = 5)
    @Size(max = 500)
    private final String invitationText;
    @ApiModelProperty(value = "Text about volunteers", example = "TOZ volunteers are people who..", position = 6)
    @Size(max = 500)
    private final String volunteerText;

    @JsonPOJOBuilder(withPrefix = "")
    public static class OrganizationInfoViewBuilder {
    }
}
