package com.intive.patronage.toz.mail.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Registration {
    private String url;
    private String token;

    public static Registration of(String activationUrl, String token) {
        final Registration registration = new Registration();
        registration.setToken(token);
        registration.setUrl(activationUrl);

        return registration;
    }
}
