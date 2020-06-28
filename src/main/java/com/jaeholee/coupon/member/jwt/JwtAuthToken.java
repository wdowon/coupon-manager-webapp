package com.jaeholee.coupon.member.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * The type Jwt auth token.
 * Author : wdowon@gmail.com
 */
public class JwtAuthToken extends AbstractAuthenticationToken {
    private Object principal;
    private String credentials;

    /**
     * Instantiates a new Jwt auth token.
     *
     * @param principal   the principal
     * @param credentials the credentials
     */
    public JwtAuthToken(String principal, String credentials) {
        super(null);
        super.setAuthenticated(false);

        this.principal = principal;
        this.credentials = credentials;
    }

    /**
     * Instantiates a new Jwt auth token.
     *
     * @param principal   the principal
     * @param credentials the credentials
     * @param authorities the authorities
     */
    public JwtAuthToken(Object principal, String credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        super.setAuthenticated(true);

        this.principal = principal;
        this.credentials = credentials;
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }
}
