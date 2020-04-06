package com.ht.authorization.autoconfig;

import com.ht.authorization.exception.AuthenticationException;
import com.ht.authorization.exception.PermissionDeniedException;
import com.ht.tohka.common.core.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthorizationExceptionHandler {

    @ExceptionHandler(PermissionDeniedException.class)
    public Result handlePermissionDeniedException(PermissionDeniedException e) {
        return Result.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public Result handleAuthenticationException(AuthenticationException e) {
        return Result.error(401, e.getMessage());
    }
}
