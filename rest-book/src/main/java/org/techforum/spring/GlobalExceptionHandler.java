package org.techforum.spring;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.techforum.spring.book.exception.ApiCallTimeoutException;
import org.techforum.spring.maintenance.exception.MaintenanceException;

/**
 * Handles all the exceptions thrown by the application
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Indicates that the application is on maintenance
     */
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    @ExceptionHandler(MaintenanceException.class)
    public void maintenance() {
        /* nothing to do */
    }

    /**
     * Indicates there is a timeout
     */
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    @ExceptionHandler({ApiCallTimeoutException.class})
    public void timeoutException() {
    }


    /**
     * Any other exception
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({RuntimeException.class, Exception.class})
    public void anyException() {
    }
}
