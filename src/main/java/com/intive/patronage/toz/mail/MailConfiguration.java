package com.intive.patronage.toz.mail;

import javax.mail.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class MailConfiguration {
    private final static String PROPERTIES_FILE_NAME = "mail.properties";

    private MailConfiguration() {
    }

    static Properties readProperties() throws IOException {
        Properties properties = new Properties();
        InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
        properties.load(inputStream);
        inputStream.close();
        return properties;
    }

    static Session authenticate(Properties properties) {
        return Session.getDefaultInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        properties.getProperty("username"), properties.getProperty("password"));
            }
        });
    }
}
