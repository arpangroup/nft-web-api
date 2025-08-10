package com.trustai.common.exception;

public class RestCallException extends RuntimeException {
    public RestCallException(String message, Throwable cause) {
        super(message, cause);
    }
}
