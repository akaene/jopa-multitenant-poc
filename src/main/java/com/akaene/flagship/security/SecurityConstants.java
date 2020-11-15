package com.akaene.flagship.security;

public class SecurityConstants {

    /**
     * String prefix added to JWT tokens in the Authorization header.
     */
    public static final String JWT_TOKEN_PREFIX = "Bearer ";

    /**
     * JWT claim used to store user's global roles in the system.
     */
    public static final String JWT_ROLE_CLAIM = "role";

    /**
     * Delimiter used to separate roles in a JWT.
     */
    public static final String JWT_ROLE_DELIMITER = "-";

    public static final String LOGIN_URL = "/login";

    public static final String USERNAME_PARAM = "username";

    /**
     * Session timeout in milliseconds. 8 hours.
     */
    public static final int SESSION_TIMEOUT = 8 * 60 * 60 * 1000;

    private SecurityConstants() {
        throw new AssertionError();
    }
}
