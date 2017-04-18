package com.intive.patronage.toz.mail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MailServiceTest {
    @Mock
    private JavaMailSender javaMailSender;
    @InjectMocks
    private MailService service = new MailService(javaMailSender);

    @Test
    public void shouldSendMessage() throws Exception {
        String recipient = "test@test.com";
        String subject = "Test subject";
        String messageContent = "Test message";

        Session session = null;
        MimeMessage mimeMessage = new MimeMessage(session);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        service.sendMail(subject, messageContent, recipient);
        verify(javaMailSender).send(mimeMessage);
    }
}
