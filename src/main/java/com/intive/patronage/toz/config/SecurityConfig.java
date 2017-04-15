package com.intive.patronage.toz.config;

import com.intive.patronage.toz.users.UserService;
import com.intive.patronage.toz.users.model.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SuperAdminAuthenticationProvider superAdminAuthenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/admin/**").authenticated()
                .and().httpBasic()
                .and().headers().frameOptions().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(superAdminAuthenticationProvider);
    }

    @Component
    private static class SuperAdminAuthenticationProvider implements AuthenticationProvider {
        private final UserService userService;

        @Autowired
        public SuperAdminAuthenticationProvider(UserService userService) {
            this.userService = userService;
        }

        @Override
        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            final String name = authentication.getName();
            final User user = userService.findOneByName(name);
            if (user.isSuperAdmin()) { // TODO check password
                final String superAdminRole = User.Role.SA.toString();
                Collection<? extends GrantedAuthority> authorities =
                        Collections.singleton(new SimpleGrantedAuthority(superAdminRole));
                return new UsernamePasswordAuthenticationToken(name, authentication.getCredentials(), authorities);
            }
            throw new BadCredentialsException("Wrong name or password!");
        }

        @Override
        public boolean supports(Class<?> authentication) {
            return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
        }
    }
}
