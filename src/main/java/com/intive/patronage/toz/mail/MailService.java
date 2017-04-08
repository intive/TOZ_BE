package com.intive.patronage.toz.mail;

import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

@Service
public class MailService {
    private Message createMessage(Session session, String subject, String messageContent,
                                  String recipient) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(recipient));
        message.setSubject(subject);
        message.setText(messageContent);
        return message;
    }

    public void sendMail(String subject, String messageContent, String recipient)
            throws IOException, MessagingException {
        Properties properties = MailConfiguration.readProperties();
        Session session = MailConfiguration.authenticate(properties);
        Transport.send(createMessage(session, subject, messageContent, recipient));
    }
}
