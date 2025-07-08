package com.trustai.transaction_service.exception;


/**
 * Thrown to indicate that the specified payment gateway is invalid or unsupported.
 */
public class InvalidPaymentGatewayException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidPaymentGatewayException() {
        super("Invalid or unsupported payment gateway.");
    }

    public InvalidPaymentGatewayException(String message) {
        super(message);
    }

    public InvalidPaymentGatewayException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidPaymentGatewayException(Throwable cause) {
        super(cause);
    }
}