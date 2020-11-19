package com.akaene.flagship.exception.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TenantNotFoundException extends AuthenticationException {

    public TenantNotFoundException() {
        super("Login request is missing tenant information.");
    }

    public TenantNotFoundException(String msg) {
        super(msg);
    }
}
