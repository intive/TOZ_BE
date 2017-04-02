package com.intive.patronage.toz.organization.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.URL;

@ApiModel("Contact")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(builder = ContactView.Builder.class)
public class ContactView {

    @ApiModelProperty(value = "Email", example = "mail@example.com", position = 1)
    @Email
    private final String email;
    @ApiModelProperty(value = "Phone number", example = "+48123123123", position = 2)
    private final String phone;
    @ApiModelProperty(value = "Fax", example = "+48123123123", position = 3)
    private final String fax;
    @ApiModelProperty(value = "Website", example = "http://example.com/", position = 4)
    @URL
    private final String website;

    private ContactView(Builder builder) {
        this.email = builder.email;
        this.phone = builder.phone;
        this.fax = builder.fax;
        this.website = builder.website;
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

    @JsonPOJOBuilder(withPrefix = "set")
    public static class Builder {
        private String email;
        private String phone;
        private String fax;
        private String website;

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

        public ContactView build() {
            return new ContactView(this);
        }
    }
}
