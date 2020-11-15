package com.akaene.flagship.security;

import com.akaene.flagship.security.model.FlagshipUserDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Ensures that a JSON Web token is generated when user successfully logs into the application.
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtils jwtUtils;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
        setAuthenticationManager(authenticationManager);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        final FlagshipUserDetails ud = (FlagshipUserDetails) authResult.getDetails();
        final String token = jwtUtils.generateToken(ud);
        response.addHeader(HttpHeaders.AUTHORIZATION, SecurityConstants.JWT_TOKEN_PREFIX + token);
        super.successfulAuthentication(request, response, chain, authResult);
    }
}
