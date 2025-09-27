package com.gaziz.bank.shared.exception;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {
    private final String error;
    private ErrorCode errorCode;

    public ValidationException(final String message) {
        super(message);
        this.error = message;
    }

    public ValidationException(final String message, final ErrorCode errorCode) {
        super(message);
        this.error = message;
        this.errorCode = errorCode;
    }
}
