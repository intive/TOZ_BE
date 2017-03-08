package com.intive.patronage.toz.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;

@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class OrganizationInfo {

    @Id
    private final Long id;

    @NotEmpty
    private final String name;

    @Pattern(regexp = "[0-9]{2}-[0-9]{3}")
    private final String postCode;
    private final String city;
    private final String street;

    @Email
    private final String email;
    private final String phone;
    private final String fax;
    @URL
    private final String website;

    @NotEmpty
    private final String accountNumber;
    private final String bankName;

    OrganizationInfo() {
        this.id = null;
        this.name = null;
        this.postCode = null;
        this.city = null;
        this.street = null;
        this.email = null;
        this.phone = null;
        this.fax = null;
        this.website = null;
        this.accountNumber = null;
        this.bankName = null;
    }

    private OrganizationInfo(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.postCode = builder.postCode;
        this.city = builder.city;
        this.street = builder.street;
        this.email = builder.email;
        this.phone = builder.phone;
        this.fax = builder.fax;
        this.website = builder.website;
        this.accountNumber = builder.accountNumber;
        this.bankName = builder.bankName;
    }

    public Long getId() {
        return id;
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

    @JsonIgnore
    public String getFullAddress() {
        return street + " " + postCode + " " + city;
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

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public static class Builder {
        private Long id;
        private String name;
        private String postCode;
        private String city;
        private String street;
        private String email;
        private String phone;
        private String fax;
        private String website;
        private String accountNumber;
        private String bankName;

        public Builder setId(Long id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setPostCode(String postCode) {
            this.postCode = postCode;
            return this;
        }

        public Builder setCity(String city) {
            this.city = city;
            return this;
        }

        public Builder setStreet(String street) {
            this.street = street;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setPhone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder setFax(String fax) {
            this.fax = fax;
            return this;
        }

        public Builder setWebsite(String website) {
            this.website = website;
            return this;
        }

        public Builder setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
            return this;
        }

        public Builder setBankName(String bankName) {
            this.bankName = bankName;
            return this;
        }

        public OrganizationInfo build() {
            return new OrganizationInfo(this);
        }
    }
}
