package com.intive.patronage.toz.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(builder = OrganizationInfoView.Builder.class)
public class OrganizationInfoView {

    @NotEmpty
    private final String name;
    @Valid
    private final AddressView address;
    @Valid
    private final ContactView contact;
    @Valid
    private final BankAccountView bankAccount;


    private OrganizationInfoView(Builder builder) {
        this.name = builder.name;
        this.address = builder.address;
        this.contact = builder.contact;
        this.bankAccount = builder.bankAccount;
    }

    public String getName() {
        return name;
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
        private final String name;
        private final BankAccountView bankAccount;
        private AddressView address;
        private ContactView contact;

        public Builder(@JsonProperty("name") String name,
                       @JsonProperty("bankAccount") BankAccountView bankAccount) {
            this.name = name;
            this.bankAccount = bankAccount;
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
