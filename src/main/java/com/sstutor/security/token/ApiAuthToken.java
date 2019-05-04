package com.sstutor.security.token;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import com.sstutor.model.UserContext;

public class ApiAuthToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 2877954820905567502L;

    private String rawApiToken;
    private UserContext userContext;

    public ApiAuthToken(String unsafeToken) {
        super(null);
        this.rawApiToken = unsafeToken;
        this.setAuthenticated(false);
        System.out.println("hello apitoken created");
    }

    public ApiAuthToken(UserContext userContext, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.eraseCredentials();
        this.userContext = userContext;
        super.setAuthenticated(true);
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return rawApiToken;
    }

    @Override
    public Object getPrincipal() {
        return this.userContext;
    }

    @Override
    public void eraseCredentials() {        
        super.eraseCredentials();
        this.rawApiToken = null;
    }
}

