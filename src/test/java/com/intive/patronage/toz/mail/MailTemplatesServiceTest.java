package com.intive.patronage.toz.mail;

import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class MailTemplatesServiceTest {

    private static final String EXAMPLE_TOKEN = "foo";
    private static final String BASE_URL = "http://foo/bar";

    private MailTemplatesService mailTemplatesService =
            new MailTemplatesService("/templates", ".hbs",
                    BASE_URL,
                    new MailTemplateCache(1, 2));

    @Test
    public void shouldReturnMailBody() throws IOException {
        String mailBody = mailTemplatesService.getRegistrationTemplate(EXAMPLE_TOKEN);

        String expectedLink = mailTemplatesService.createActivationUrl() + "?token=" + EXAMPLE_TOKEN;
        assertThat(mailBody).contains(expectedLink);
    }
}
