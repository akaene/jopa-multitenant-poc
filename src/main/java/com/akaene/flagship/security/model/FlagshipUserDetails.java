package com.akaene.flagship.security.model;

import com.akaene.flagship.dto.UserAccountDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.net.URI;
import java.util.*;

public class FlagshipUserDetails implements UserDetails {

    /**
     * Default authority held by all registered users of the system.
     */
    public static final GrantedAuthority DEFAULT_AUTHORITY = new SimpleGrantedAuthority("ROLE_USER");

    private final UserAccountDto user;

    private URI tenant;

    private final Set<GrantedAuthority> authorities;

    public FlagshipUserDetails(UserAccountDto user) {
        Objects.requireNonNull(user);
        this.user = user;
        this.authorities = resolveAuthorities(user);
    }

    public FlagshipUserDetails(UserAccountDto user, Collection<GrantedAuthority> authorities) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(authorities);
        this.user = user;
        this.authorities = resolveAuthorities(user);
        this.authorities.addAll(authorities);
    }

    private static Set<GrantedAuthority> resolveAuthorities(UserAccountDto user) {
        final Set<GrantedAuthority> authorities = new HashSet<>(4);
        authorities.add(DEFAULT_AUTHORITY);
        return authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.unmodifiableCollection(authorities);
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    public URI getUserId() {
        return user.getUri();
    }

    public void setTenant(URI tenant) {
        this.tenant = tenant;
    }

    public URI getTenant() {
        return tenant;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FlagshipUserDetails)) {
            return false;
        }
        FlagshipUserDetails that = (FlagshipUserDetails) o;
        return Objects.equals(user, that.user) && Objects.equals(authorities, that.authorities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, authorities);
    }

    @Override
    public String toString() {
        return "UserDetails{" +
                "user=" + user +
                ", tenant=" + tenant +
                ", authorities=" + authorities +
                '}';
    }
}
