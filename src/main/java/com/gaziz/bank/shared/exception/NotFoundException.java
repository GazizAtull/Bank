package com.gaziz.bank.shared.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {
    private final String error;

    public NotFoundException(final String error) {
        super(error);
        this.error = error;
    }
}
