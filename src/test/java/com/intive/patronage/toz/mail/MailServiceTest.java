package com.intive.patronage.toz.mail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import org.junit.Before;
import org.junit.Test;
import org.jvnet.mock_javamail.Mailbox;

public class MailServiceTest {
    private MailService mailService;

    @Before
    public void setUp() {
        mailService = new MailService();
        Mailbox.clearAll();
    }

    @Test
    public void sendMailTest() throws MessagingException, IOException {
        String subject = "Some subject";
        String body = "Some contents.";
        String recipient = "test@test.com";

        mailService.sendMail(subject, body, recipient);
        List<Message> inbox = Mailbox.get(recipient);
        assertTrue(inbox.size() == 1);
        assertEquals(subject, inbox.get(0).getSubject());
        assertEquals(body, inbox.get(0).getContent());
        assertEquals("text/plain; charset=us-ascii", inbox.get(0).getContentType());
        assertEquals(new InternetAddress(recipient), inbox.get(0).getRecipients(Message.RecipientType.TO)[0]);
    }
}
