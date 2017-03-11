package com.intive.patronage.toz.model.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.intive.iban.validator.IbanFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("Bank account")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonDeserialize(builder = BankAccountView.Builder.class)
public class BankAccountView {

    @ApiModelProperty(value = "Number", required = true, example = "61109010140000071219812874")
    @IbanFormat
    private final String number;
    @ApiModelProperty(value = "Bank name")
    private final String bankName;

    private BankAccountView(Builder builder) {
        this.number = builder.number;
        this.bankName = builder.bankName;
    }

    public String getNumber() {
        return number;
    }

    public String getBankName() {
        return bankName;
    }

    @JsonPOJOBuilder(withPrefix = "set")
    public static class Builder {
        private final String number;
        private String bankName;

        public Builder(@JsonProperty("number") String number) {
            this.number = number;
        }

        public Builder setBankName(String bankName) {
            this.bankName = bankName;
            return this;
        }

        public BankAccountView build() {
            return new BankAccountView(this);
        }
    }
}
