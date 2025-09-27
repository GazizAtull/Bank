package com.gaziz.bank.shared.validation.base;

import java.util.ArrayList;
import java.util.List;

/**
 * Универсальный валидатор который позволяет применять цепочку правил валидации
 * к входным данным и получать результат после прохождения всех проверок.
 *
 * <p>Валидатор поддерживает два типа правил:
 * <ul>
 *   <li>Первое правило - преобразует входной тип R в промежуточный тип T</li>
 *   <li>Последующие правила - работают с типом T и возвращают T</li>
 * </ul>
 *
 * <p>Пример использования:
 * <pre>{@code
 * UniversalValidator<Document, UUID> validator = new UniversalValidator<Document, UUID>()
 *     .start()
 *     .withFirstRule(new DocumentExistsRule(documentUCase))
 *     .then(new DocumentActiveRule())
 *     .validate(documentId);
 *
 * }</pre>
 *
 * @param <T> тип результата валидации
 * @param <R> тип входных данных
 */
public class UniversalValidator<T, R> {
    private final List<ValidationRule<?, ?>> rules = new ArrayList<>();

    public UniversalValidator() {}

    public UniversalValidator<T, R> start() {
        return this;
    }

    // Метод для первого правила - принимает правило, которое преобразует R в T
    public <I> UniversalValidator<T, R> withFirstRule(ValidationRule<T, I> firstRule) {
        rules.add(firstRule);
        return this;
    }

    // Метод для последующих правил - принимает правило, которое работает с T
    public UniversalValidator<T, R> then(ValidationRule<T, T> rule) {
        rules.add(rule);
        return this;
    }

    @SuppressWarnings("unchecked")
    public T validate(R input) {
        Object result = input;
        for (ValidationRule<?, ?> rule : rules) {
            result = ((ValidationRule<Object, Object>) rule).validate(result);
        }
        return (T) result;
    }
}
