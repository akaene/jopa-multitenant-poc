package com.akaene.flagship.security;

import com.akaene.flagship.security.model.AuthenticationToken;
import com.akaene.flagship.security.model.FlagshipUserDetails;
import com.akaene.flagship.service.repository.TenantService;
import com.akaene.flagship.service.security.SecurityUtils;
import com.akaene.flagship.service.security.UserDetailsService;
import com.akaene.flagship.util.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Objects;

@Service
public class OntologyAuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOG = LoggerFactory.getLogger(OntologyAuthenticationProvider.class);

    private final UserDetailsService userDetailsService;

    private final TenantService tenantService;

    private final HttpUtils httpUtils;

    @Autowired
    public OntologyAuthenticationProvider(UserDetailsService userDetailsService, TenantService tenantService,
                                          HttpUtils httpUtils) {
        this.userDetailsService = userDetailsService;
        this.tenantService = tenantService;
        this.httpUtils = httpUtils;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = authentication.getPrincipal().toString();
        verifyUsernameNotEmpty(username);
        final URI tenant = httpUtils.resolveTenant();
        ensureTenantRepositoryIsConnected();
        LOG.debug("Authenticating user '{}' under tenant <{}>.", username, tenant);
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

    private void ensureTenantRepositoryIsConnected() {
        tenantService.connectToTenantRepository();
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass) || AuthenticationToken.class
                .isAssignableFrom(aClass);
    }
}
