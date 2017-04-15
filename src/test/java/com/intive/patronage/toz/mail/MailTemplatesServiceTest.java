package com.intive.patronage.toz.mail;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class MailTemplatesServiceTest {
    private static final String EXPECTED_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWV9.TJVA95OrM7E2cBab30RMHrHDcEfxjoYZgeFONFh7HgQ";
    private MailTemplatesService mailTemplatesService;

    @Before
    public void setUp(){
        mailTemplatesService = new MailTemplatesService();
    }

    @Test
    public void shouldReturnMailBody() throws IOException {
        String mailBody = mailTemplatesService.getRegistrationTemplate("localhost", EXPECTED_TOKEN);
        assertTrue(mailBody.contains(String.format("?token=%s", EXPECTED_TOKEN)));
    }
}
