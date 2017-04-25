package com.intive.patronage.toz.config;

import com.intive.patronage.toz.tokens.auth.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String REST_ENTRY_POINT = "/**";
    private static final String ADMIN_ENTRY_POINT = "/admin/**";

    @Value("${jwt.secret-base64}")
    private String secret;

    @Autowired
    private SuperAdminAuthenticationProvider superAdminAuthenticationProvider;

    private TokenAuthenticationFilter getAuthenticationFilter() throws Exception {
        final List<String> pathsToSkip = Arrays.asList(ApiUrl.ACQUIRE_TOKEN_PATH, ADMIN_ENTRY_POINT);
        final AuthenticationRequestMatcher matcher = new AuthenticationRequestMatcher(pathsToSkip);
        final TokenAuthenticationFilter filter =
                new TokenAuthenticationFilter(new TokenAuthenticationFailureHandler(), matcher);
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(superAdminAuthenticationProvider);
        auth.authenticationProvider(new JwtAuthenticationProvider(secret));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .exceptionHandling().authenticationEntryPoint(new UnauthorizedEntryPoint())
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests().antMatchers(ApiUrl.ACQUIRE_TOKEN_PATH).permitAll()
                .and().authorizeRequests().antMatchers(REST_ENTRY_POINT).authenticated()
                .and().authorizeRequests().antMatchers(ADMIN_ENTRY_POINT).authenticated()
                .and().httpBasic()
                .and().addFilterBefore(getAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
