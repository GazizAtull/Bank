package com.gaziz.bank.shared.exception;

public enum ErrorCode {
    NOT_FOUND("Записи по данному fileId не найдено"),
    ACCESS_MISMATCH("");

    private final String value;

    public String getValue() {
        return value;
    }

    ErrorCode(String value) {
        this.value = value;
    }
}
