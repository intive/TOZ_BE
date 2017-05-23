package com.intive.patronage.toz.helper.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.intive.patronage.toz.base.model.IdentifiableView;
import com.intive.patronage.toz.helper.model.db.Helper;
import com.intive.patronage.toz.util.validation.EnumValidate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Size;

@ApiModel(value = "Helper")
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class HelperView extends IdentifiableView {
    @ApiModelProperty(example = "Jan", position = 1)
    @Size(max = 35)
    private String name;

    @ApiModelProperty(example = "Kowalski", position = 2)
    @Size(max = 35)
    private String surname;

    @ApiModelProperty(example = "Dom tymczasowy tylko dla psów o " +
            "mniejszych rozmariach.", position = 3)
    @Size(max = 500)
    private String notes;

    @ApiModelProperty(example = "914123123", position = 4)
    @Size(max = 11)
    private String phoneNumber;

    @ApiModelProperty(example = "jankowalski@gmail.com", position = 5)
    @Email
    @Size(max = 255)
    private String email;

    @ApiModelProperty(example = "TEMPORARY_HOUSE_DOG",
            allowableValues = "GUARDIAN, TEMPORARY_HOUSE_CAT," +
                    " TEMPORARY_HOUSE_DOG, TEMPORARY_HOUSE_OTHER",
            position = 6)
    @EnumValidate(enumClass = Helper.Category.class)
    private String category;

    @ApiModelProperty(example = "1222333444555", position = 7)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long created;

    @ApiModelProperty(example = "1222333444555", position = 8)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long lastModified;
}
