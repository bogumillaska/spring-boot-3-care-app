package com.blaska.care.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CustomerCaseNotFoundException extends RuntimeException {

    public CustomerCaseNotFoundException(final String message) {
        super(message);
    }

}
