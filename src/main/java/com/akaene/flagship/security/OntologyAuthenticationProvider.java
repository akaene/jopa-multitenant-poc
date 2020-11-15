package com.akaene.flagship.security;

import com.akaene.flagship.security.model.AuthenticationToken;
import com.akaene.flagship.security.model.FlagshipUserDetails;
import com.akaene.flagship.service.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service("ontologyAuthenticationProvider")
public class OntologyAuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOG = LoggerFactory.getLogger(OntologyAuthenticationProvider.class);

    private final UserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public OntologyAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String username = authentication.getPrincipal().toString();
        verifyUsernameNotEmpty(username);
        LOG.debug("Authenticating user {}", username);

        final FlagshipUserDetails userDetails = (FlagshipUserDetails) userDetailsService.loadUserByUsername(username);
        final String password = (String) authentication.getCredentials();
        if (!Objects.equals(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Provided credentials don't match.");
        }
        return SecurityUtils.setCurrentUser(userDetails);
    }

    private void verifyUsernameNotEmpty(String username) {
        if (username.isEmpty()) {
            throw new UsernameNotFoundException("Username cannot be empty.");
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass) || AuthenticationToken.class
                .isAssignableFrom(aClass);
    }
}
