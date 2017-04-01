package com.intive.patronage.toz.organization.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Pattern;

@ApiModel("Address")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(builder = AddressView.Builder.class)
public class AddressView {

    @ApiModelProperty(value = "Post code", example = "02-123", position = 4)
    @Pattern(regexp = "[0-9]{2}-[0-9]{3}")
    private final String postCode;
    @ApiModelProperty(value = "City", position = 5)
    private final String city;
    @ApiModelProperty(value = "Street", example = "ul. Przykładowa", position = 1)
    private final String street;
    @ApiModelProperty(value = "Country", position = 6)
    private final String country;
    @ApiModelProperty(value = "House number", example = "1", position = 2)
    private final String houseNumber;
    @ApiModelProperty(value = "Apartment number", example = "1", position = 3)
    private final String apartmentNumber;

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

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static class Builder {
        private final String postCode;
        private final String city;
        private final String street;
        private String country;
        private String houseNumber;
        private String apartmentNumber;

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

        public Builder setHouseNumber(String houseNumber) {
            this.houseNumber = houseNumber;
            return this;
        }

        public Builder setApartmentNumber(String apartmentNumber) {
            this.apartmentNumber = apartmentNumber;
            return this;
        }

        public AddressView build() {
            return new AddressView(this);
        }
    }
}
