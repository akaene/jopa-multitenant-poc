package com.akaene.flagship.security;

import com.akaene.flagship.exception.security.TenantNotFoundException;
import com.akaene.flagship.security.model.AuthenticationToken;
import com.akaene.flagship.security.model.FlagshipUserDetails;
import com.akaene.flagship.service.security.SecurityUtils;
import com.akaene.flagship.service.security.UserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Objects;

@Service
public class OntologyAuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOG = LoggerFactory.getLogger(OntologyAuthenticationProvider.class);

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    private final HttpServletRequest loginRequest;

    @Autowired
    public OntologyAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder,
                                          HttpServletRequest loginRequest) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.loginRequest = loginRequest;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = authentication.getPrincipal().toString();
        verifyUsernameNotEmpty(username);
        LOG.debug("Authenticating user {}", username);
        final URI tenant = resolveTenant();
        final FlagshipUserDetails userDetails = userDetailsService.loadUserByUsername(username);
        final String password = (String) authentication.getCredentials();
        if (!Objects.equals(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Provided credentials don't match.");
        }
        userDetails.setTenant(tenant);
        return SecurityUtils.setCurrentUser(userDetails);
    }

    private void verifyUsernameNotEmpty(String username) {
        if (username.isEmpty()) {
            throw new UsernameNotFoundException("Username cannot be empty.");
        }
    }

    private URI resolveTenant() {
        final String tenantIri = loginRequest.getHeader(SecurityConstants.TENANT_HEADER);
        if (tenantIri == null || tenantIri.trim().isEmpty()) {
            throw new TenantNotFoundException();
        }
        return URI.create(tenantIri);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass) || AuthenticationToken.class
                .isAssignableFrom(aClass);
    }
}
