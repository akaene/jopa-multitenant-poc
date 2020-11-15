package com.akaene.flagship.security;

import com.akaene.flagship.exception.security.JwtException;
import com.akaene.flagship.rest.handler.ErrorInfo;
import com.akaene.flagship.security.model.FlagshipUserDetails;
import com.akaene.flagship.service.security.SecurityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This filter retrieves JWT from the incoming request and validates it, ensuring that the user is authorized to access
 * the application.
 */
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final JwtUtils jwtUtils;

    private final ObjectMapper objectMapper;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, JwtUtils jwtUtils,
                                  ObjectMapper objectMapper) {
        super(authenticationManager);
        this.jwtUtils = jwtUtils;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith(SecurityConstants.JWT_TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        final String authToken = authHeader.substring(SecurityConstants.JWT_TOKEN_PREFIX.length());
        try {
            final FlagshipUserDetails userDetails = jwtUtils.extractUserInfo(authToken);
            SecurityUtils.setCurrentUser(userDetails);
            refreshToken(authToken, response);
        } catch (DisabledException | LockedException | JwtException | UsernameNotFoundException e) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            objectMapper.writeValue(response.getOutputStream(),
                    ErrorInfo.createWithMessage(e.getMessage(), request.getRequestURI()));
            return;
        }
        chain.doFilter(request, response);
    }

    private void refreshToken(String authToken, HttpServletResponse response) {
        final String newToken = jwtUtils.refreshToken(authToken);
        response.setHeader(HttpHeaders.AUTHORIZATION, SecurityConstants.JWT_TOKEN_PREFIX + newToken);
    }
}
