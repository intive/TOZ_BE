package com.intive.patronage.toz.organization.model.db;

import com.intive.patronage.toz.base.model.Identifiable;
import com.intive.patronage.toz.organization.model.view.AddressView;
import com.intive.patronage.toz.organization.model.view.BankAccountView;
import com.intive.patronage.toz.organization.model.view.ContactView;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.UUID;

@Getter
@NoArgsConstructor
@Entity
public class OrganizationInfo extends Identifiable {

    private String name;
    private String invitationText;
    private String volunteerText;

    private String postCode;
    private String city;
    private String street;
    private String country;
    private String houseNumber;
    private String apartmentNumber;

    private String email;
    private String phone;
    private String fax;
    private String website;

    private String bankAccountNumber;
    private String bankName;

    private OrganizationInfo(Builder builder) {
        this.setId(builder.id);
        this.name = builder.name;
        this.invitationText = builder.invitationText;
        this.volunteerText = builder.volunteerText;

        if (builder.address != null) {
            this.postCode = builder.address.getPostCode();
            this.city = builder.address.getCity();
            this.street = builder.address.getStreet();
            this.country = builder.address.getCountry();
            this.houseNumber = builder.address.getHouseNumber();
            this.apartmentNumber = builder.address.getApartmentNumber();
        }

        if (builder.contact != null) {
            this.email = builder.contact.getEmail();
            this.phone = builder.contact.getPhone();
            this.fax = builder.contact.getFax();
            this.website = builder.contact.getWebsite();
        }

        if (builder.bankAccount != null) {
            this.bankAccountNumber = builder.bankAccount.getNumber();
            this.bankName = builder.bankAccount.getBankName();
        }
    }

    public static class Builder {
        private final UUID id;
        private final String name;
        private final String invitationText;
        private final String volunteerText;
        private AddressView address;
        private ContactView contact;
        private BankAccountView bankAccount;

        public Builder(UUID id, String name, String invitationText, String volunteerText) {
            this.id = id;
            this.name = name;
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

        public Builder setBankAccount(BankAccountView bankAccount) {
            this.bankAccount = bankAccount;
            return this;
        }

        public OrganizationInfo build() {
            return new OrganizationInfo(this);
        }
    }
}
