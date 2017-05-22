package com.intive.patronage.toz.helper.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.intive.patronage.toz.base.model.IdentifiableView;
import com.intive.patronage.toz.helper.model.db.Helper;
import com.intive.patronage.toz.util.validation.EnumValidate;
import com.intive.patronage.toz.util.validation.Phone;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Size;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class HelperView extends IdentifiableView {
    @Size(min = 1, max = 35)
    private String name;

    @Size(min = 1, max = 35)
    private String surname;

    @Size(min = 1, max = 500)
    private String notes;

    @Phone
    private String phoneNumber;

    @Email
    @Size(min = 1, max = 255)
    private String email;

    @EnumValidate(enumClass = Helper.Category.class)
    private String category;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long created;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long lastModified;
}
