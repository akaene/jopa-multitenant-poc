/**
 * TermIt
 * Copyright (C) 2019 Czech Technical University in Prague
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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
                ", authorities=" + authorities +
                '}';
    }
}
