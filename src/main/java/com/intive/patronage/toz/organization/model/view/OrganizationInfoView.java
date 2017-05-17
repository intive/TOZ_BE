package com.intive.patronage.toz.organization.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

@ApiModel("Organization information")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(builder = OrganizationInfoView.Builder.class)
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

    private OrganizationInfoView(Builder builder) {
        this.name = builder.name;
        this.invitationText = builder.invitationText;
        this.volunteerText = builder.volunteerText;
        this.address = builder.address;
        this.contact = builder.contact;
        this.bankAccount = builder.bankAccount;
    }

    public String getName() {
        return name;
    }

    public String getInvitationText() {
        return invitationText;
    }

    public String getVolunteerText() {
        return volunteerText;
    }

    public AddressView getAddress() {
        return address;
    }

    public ContactView getContact() {
        return contact;
    }

    public BankAccountView getBankAccount() {
        return bankAccount;
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static class Builder {
        private final String invitationText;
        private final String volunteerText;
        private final String name;
        private final BankAccountView bankAccount;
        private AddressView address;
        private ContactView contact;

        public Builder(@JsonProperty("name") String name,
                       @JsonProperty("bankAccount") BankAccountView bankAccount,
                       @JsonProperty("invitationText") String invitationText,
                       @JsonProperty("invitationText") String volunteerText) {
            this.name = name;
            this.bankAccount = bankAccount;
            this.invitationText = invitationText;
            this.volunteerText = volunteerText;
        }

        public Builder setAddress(AddressView address) {
            this.address = address;
            return this;
        }

        public Builder setContact(ContactView contact) {
            this.contact = contact;
            return this;
        }

        public OrganizationInfoView build() {
            return new OrganizationInfoView(this);
        }
    }
}
