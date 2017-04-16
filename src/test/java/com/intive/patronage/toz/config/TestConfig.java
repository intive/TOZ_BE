package com.intive.patronage.toz.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.TestPropertySource;

@TestConfiguration
@TestPropertySource(properties = "jwt.secret-base64=c2VjcmV0")
public class TestConfig {
}
