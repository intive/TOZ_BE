package com.intive.patronage.toz.model.db;

import com.intive.patronage.toz.model.view.AddressView;
import com.intive.patronage.toz.model.view.BankAccountView;
import com.intive.patronage.toz.model.view.ContactView;

import javax.persistence.Entity;
import java.util.UUID;

@Entity
public class OrganizationInfo extends Identifiable {

    private String name;

    private String postCode;
    private String city;
    private String street;
    private String country;
    private Integer houseNumber;
    private Integer apartmentNumber;

    private String email;
    private String phone;
    private String fax;
    private String website;

    private String bankAccountNumber;
    private String bankName;

    OrganizationInfo() {
    }

    private OrganizationInfo(Builder builder) {
        this.setId(builder.id);
        this.name = builder.name;

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

    public String getName() {
        return name;
    }

    public String getPostCode() {
        return postCode;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getCountry() {
        return country;
    }

    public Integer getHouseNumber() {
        return houseNumber;
    }

    public Integer getApartmentNumber() {
        return apartmentNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getFax() {
        return fax;
    }

    public String getWebsite() {
        return website;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public static class Builder {
        private final UUID id;
        private final String name;
        private AddressView address;
        private ContactView contact;
        private BankAccountView bankAccount;

        public Builder(UUID id, String name) {
            this.id = id;
            this.name = name;
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
