package com.akaene.flagship.security.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.security.Principal;
import java.util.Collection;
import java.util.Objects;

public class AuthenticationToken extends AbstractAuthenticationToken implements Principal {

    private final FlagshipUserDetails userDetails;

    public AuthenticationToken(Collection<? extends GrantedAuthority> authorities, FlagshipUserDetails userDetails) {
        super(authorities);
        this.userDetails = userDetails;
        super.setAuthenticated(true);
        super.setDetails(userDetails);
    }

    @Override
    public Object getCredentials() {
        return userDetails.getPassword();
    }

    @Override
    public Object getPrincipal() {
        return userDetails;
    }

    @Override
    public FlagshipUserDetails getDetails() {
        return userDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AuthenticationToken)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        AuthenticationToken that = (AuthenticationToken) o;
        return Objects.equals(userDetails, that.userDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userDetails);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
