package me.hchome.demouser.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.OptimisticLockException;

/**
 * @author Junjie(Cliff) Pan
 */
@RestControllerAdvice
public class ErrorHandlers {

    @ExceptionHandler(OptimisticLockException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST,reason = "Version error")
    public Error handleVersionError(OptimisticLockException ex){
        return new Error(ex);
    }
}
