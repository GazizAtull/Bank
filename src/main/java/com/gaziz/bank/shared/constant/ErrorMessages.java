package com.gaziz.bank.shared.constant;

/**
 * Константы для сообщений об ошибках
 */
public final class ErrorMessages {
    
    private ErrorMessages() {
        // Утилитный класс
    }

    /**
     * Форматирует сообщение с параметрами
     */
    public static String format(String template, Object... args) {
        return String.format(template, args);
    }
} 