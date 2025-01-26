package com.study.userity.exception;

import javax.naming.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InvalidJWTAuthentication extends AuthenticationException {
    public InvalidJWTAuthentication(String ex) { super(ex); }
}
