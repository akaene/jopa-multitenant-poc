package com.akaene.flagship.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public static NotFoundException create(Class<?> type, Object identifier) {
        return new NotFoundException(type.getSimpleName() + " identified by " + identifier + " not found.");
    }
}
