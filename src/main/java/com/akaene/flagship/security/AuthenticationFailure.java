package com.akaene.flagship.security;

import com.akaene.flagship.security.model.LoginStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Returns info about authentication failure.
 */
@Service
public class AuthenticationFailure implements AuthenticationFailureHandler {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationFailure.class);

    private final ObjectMapper mapper;

    @Autowired
    public AuthenticationFailure(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                        AuthenticationException e) throws IOException {
        LOG.trace("Login failed for user {}.", httpServletRequest.getParameter(SecurityConstants.USERNAME_PARAM));
        final LoginStatus status = new LoginStatus(false, false, null, e.getMessage());
        if (e instanceof LockedException) {
            status.setErrorId("login.locked");
        } else if (e instanceof DisabledException) {
            status.setErrorId("login.disabled");
        } else if (e instanceof UsernameNotFoundException) {
            status.setErrorId("login.error");
        }
        mapper.writeValue(httpServletResponse.getOutputStream(), status);
    }
}
