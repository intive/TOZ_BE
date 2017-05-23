package com.intive.patronage.toz.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailService {
    private static final boolean ENABLE_MULTIPART = true;
    private JavaMailSender javaMailSender;

    @Autowired
    public MailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    public void sendMail(String subject, String messageContent, String recipient)
            throws MessagingException {
        sendMail(subject, messageContent, recipient, null, null);
    }

    @Async
    public void sendMail(String subject, String messageContent, String recipient,
                         String attachmentFileName, InputStreamSource attachmentFile)
            throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, ENABLE_MULTIPART);
        messageHelper.setTo(recipient);
        messageHelper.setSubject(subject);
        messageHelper.setText(messageContent);
        if (attachmentFileName != null) {
            messageHelper.addAttachment(attachmentFileName, attachmentFile);
        }
        javaMailSender.send(mimeMessage);
    }
}
