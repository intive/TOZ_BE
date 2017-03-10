package com.intive.patronage.toz.model.view;

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

    @ApiModelProperty(value = "Post code", example = "02-123")
    @Pattern(regexp = "[0-9]{2}-[0-9]{3}")
    private final String postCode;
    @ApiModelProperty(value = "City")
    private final String city;
    @ApiModelProperty(value = "Street")
    private final String street;
    @ApiModelProperty(value = "Country")
    private final String country;
    @ApiModelProperty(value = "House number")
    private final Integer houseNumber;
    @ApiModelProperty(value = "Apartment number")
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
