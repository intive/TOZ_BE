package com.intive.patronage.toz.config;

import com.intive.patronage.toz.tokens.auth.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(2)
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String REST_ENTRY_POINT = "/**";

    @Value("${bcrypt.security.level}")
    private int securityLevel;

    @Value("${jwt.secret-base64}")
    private String secret;

    private TokenAuthenticationFilter getAuthenticationFilter() throws Exception {
        final List<String> pathsToSkip = Collections.singletonList(ApiUrl.ACQUIRE_TOKEN_PATH);
        final AuthenticationRequestMatcher matcher = new AuthenticationRequestMatcher(pathsToSkip);
        final TokenAuthenticationFilter filter =
                new TokenAuthenticationFilter(new TokenAuthenticationFailureHandler(), matcher);
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        final SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(new byte[20]);
        return new BCryptPasswordEncoder(securityLevel, secureRandom);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new JwtAuthenticationProvider(secret));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling().authenticationEntryPoint(new UnauthorizedEntryPoint())
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests().antMatchers(ApiUrl.ACQUIRE_TOKEN_PATH).permitAll()
                .and().authorizeRequests().antMatchers(REST_ENTRY_POINT).authenticated()
                .and().addFilterBefore(getAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
