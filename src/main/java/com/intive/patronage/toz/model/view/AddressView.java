package com.intive.patronage.toz.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import javax.validation.constraints.Pattern;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(builder = AddressView.Builder.class)
public class AddressView {

    @Pattern(regexp = "[0-9]{2}-[0-9]{3}")
    private final String postCode;
    private final String city;
    private final String street;
    private final String country;
    private final Integer houseNumber;
    private final Integer apartmentNumber;

    private AddressView(Builder builder) {
        this.postCode = builder.postCode;
        this.city = builder.city;
        this.street = builder.street;
        this.country = builder.country;
        this.houseNumber = builder.houseNumber;
        this.apartmentNumber = builder.apartmentNumber;
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

    @JsonPOJOBuilder(withPrefix = "set")
    public static class Builder {
        private final String postCode;
        private final String city;
        private final String street;
        private String country;
        private Integer houseNumber;
        private Integer apartmentNumber;

        public Builder(@JsonProperty("postCode") String postCode,
                       @JsonProperty("city") String city,
                       @JsonProperty("street") String street) {
            this.postCode = postCode;
            this.city = city;
            this.street = street;
        }

        public Builder setCountry(String country) {
            this.country = country;
            return this;
        }

        public Builder setHouseNumber(Integer houseNumber) {
            this.houseNumber = houseNumber;
            return this;
        }

        public Builder setApartmentNumber(Integer apartmentNumber) {
            this.apartmentNumber = apartmentNumber;
            return this;
        }

        public AddressView build() {
            return new AddressView(this);
        }
    }
}
