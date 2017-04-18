package com.intive.patronage.toz.tokens.auth;

import com.intive.patronage.toz.tokens.model.UserContext;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final UserContext userContext;
    private String token;

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     *                    represented by this authentication object.
     */
    JwtAuthenticationToken(UserContext userContext, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        eraseCredentials();
        this.userContext = userContext;
        super.setAuthenticated(true);
    }

    JwtAuthenticationToken(String token) {
        super(null);
        this.token = token;
        userContext = null;
        super.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return userContext;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        token = null;
    }
}
