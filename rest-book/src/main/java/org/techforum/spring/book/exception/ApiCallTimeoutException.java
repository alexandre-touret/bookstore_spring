package org.techforum.spring.book.exception;

public class ApiCallTimeoutException extends RuntimeException {
    public ApiCallTimeoutException() {
        super();
    }

    public ApiCallTimeoutException(String message) {
        super(message);
    }

    public ApiCallTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiCallTimeoutException(Throwable cause) {
        super(cause);
    }

    protected ApiCallTimeoutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
