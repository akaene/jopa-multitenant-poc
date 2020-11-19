package com.akaene.flagship.service.security;

import com.akaene.flagship.security.model.AuthenticationToken;
import com.akaene.flagship.security.model.FlagshipUserDetails;
import com.akaene.flagship.util.HttpUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
public class SecurityUtils {

    private final HttpUtils httpUtils;

    public SecurityUtils(HttpUtils httpUtils) {
        this.httpUtils = httpUtils;
    }

    public URI getCurrentTenant() {
        final SecurityContext context = SecurityContextHolder.getContext();
        if (context.getAuthentication() != null && context.getAuthentication()
                                                          .getDetails() instanceof FlagshipUserDetails) {
            return ((FlagshipUserDetails) context.getAuthentication().getDetails()).getTenant();
        } else {
            return httpUtils.resolveTenant();
        }
    }

    /**
     * Gets details of the currently authenticated user.
     *
     * @return Currently authenticated user details or null, if no one is currently authenticated
     */
    public static FlagshipUserDetails getCurrentUserDetails() {
        final SecurityContext context = SecurityContextHolder.getContext();
        if (context.getAuthentication() != null && context.getAuthentication()
                                                          .getDetails() instanceof FlagshipUserDetails) {
            return (FlagshipUserDetails) context.getAuthentication().getDetails();
        } else {
            throw new RuntimeException("Unauthorized!");
        }
    }

    /**
     * Creates an authentication token based on the specified user details and sets it to the current thread's security
     * context.
     *
     * @param userDetails Details of the user to set as current
     * @return The generated authentication token
     */
    public static AuthenticationToken setCurrentUser(FlagshipUserDetails userDetails) {
        final AuthenticationToken token = new AuthenticationToken(userDetails.getAuthorities(), userDetails);
        token.setAuthenticated(true);

        final SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(token);
        SecurityContextHolder.setContext(context);
        return token;
    }
}
