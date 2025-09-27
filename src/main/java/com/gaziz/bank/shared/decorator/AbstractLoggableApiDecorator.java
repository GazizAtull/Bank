package com.gaziz.bank.shared.decorator;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Абстрактный декоратор для логирования выполнения бизнес-операций (UseCase).
 * <p>
 * Генерирует уникальный traceId для каждого вызова, логирует параметры, результат, время выполнения и ошибки.
 * Использует MDC для передачи traceId в логи.
 * </p>
 */
@Slf4j
public class AbstractLoggableApiDecorator {
    private final ObjectMapper objectMapper;
    private static final String TRACE_ID_KEY = "X-Trace-Id";

    public AbstractLoggableApiDecorator() {
        this.objectMapper = new ObjectMapper();
    }

    private String generateTraceId() {
        return UUID.randomUUID().toString();
    }

    private void setTraceId(String traceId) {
        MDC.put(TRACE_ID_KEY, traceId);
    }

    private void clearTraceId() {
        MDC.remove(TRACE_ID_KEY);
    }

    /**
     * Выполняет void-операцию с логированием параметров, traceId, времени выполнения и ошибок.
     * <p>
     * Генерирует traceId, логирует входные параметры, время выполнения и ошибки.
     * </p>
     *
     * @param nameMethod имя метода/операции для логов
     * @param operation  операция, которую нужно выполнить
     * @param request    параметры операции
     * @param <T>        тип параметра
     */
    @SneakyThrows
    protected <T> void processingVoid(
            final String nameMethod,
            final Consumer<T> operation,
            final T request
    ) {
        final String traceId = generateTraceId();
        setTraceId(traceId);
        final long startTime = System.nanoTime();

        try {
            operation.accept(request);
            log.info("processing {}({}) UID {} TIME {} ms - VOID operation",
                    nameMethod,
                    this.objectMapper.writeValueAsString(request),
                    MDC.get("X-Trace-Id"),
                    (System.nanoTime() - startTime) / 1_000_000D
            );
        } catch (Exception exception) {
            log.error("processing {}({}) UID {} TIME {} ms - VOID operation failed with error {}",
                    nameMethod,
                    this.objectMapper.writeValueAsString(request),
                    MDC.get("X-Trace-Id"),
                    (System.nanoTime() - startTime) / 1_000_000D,
                    exception.getMessage()
            );
            throw exception;
        } finally {
            clearTraceId();
        }
    }

    /**
     * Выполняет операцию с логированием параметров, traceId, времени выполнения и ошибок.
     * <p>
     * Генерирует traceId, логирует входные параметры, время выполнения и ошибки.
     * </p>
     *
     * @param nameMethod имя метода/операции для логов
     * @param operation  операция, которую нужно выполнить (лямбда или метод)
     * @param params     параметры операции для логирования
     */
    @SneakyThrows
    protected void processing(
            final String nameMethod,
            final Runnable operation,
            final Object... params
    ) {
        final String traceId = generateTraceId();
        setTraceId(traceId);
        final long startTime = System.nanoTime();

        try {
            operation.run();
            log.info("processing {}({}) UID {} TIME {} ms - VOID operation",
                    nameMethod,
                    this.objectMapper.writeValueAsString(params),
                    MDC.get("X-Trace-Id"),
                    (System.nanoTime() - startTime) / 1_000_000D
            );
        } catch (Exception exception) {
            log.error("processing {}({}) UID {} TIME {} ms - VOID operation failed with error {}",
                    nameMethod,
                    this.objectMapper.writeValueAsString(params),
                    MDC.get("X-Trace-Id"),
                    (System.nanoTime() - startTime) / 1_000_000D,
                    exception.getMessage()
            );
            throw exception;
        } finally {
            clearTraceId();
        }
    }

    /**
     * Выполняет операцию с логированием параметров, traceId, времени выполнения и ошибок.
     * <p>
     * Перегруженный вариант для передачи параметров в виде массива.
     * </p>
     *
     * @param nameMethod имя метода/операции для логов
     * @param result     результат выполнения операции
     * @param params     параметры операции
     * @param <R>        тип результата
     * @return результат выполнения операции
     */
    @SafeVarargs
    protected final <R> R processing(
            final String nameMethod,
            final R result,
            final Object... params
    ) {
        return processing(nameMethod, result, Arrays.asList(params));
    }

    @SneakyThrows
    private <R> R processing(
            final String nameMethod,
            final R result,
            final List<Object> params
    ) {
        final String traceId = generateTraceId();
        setTraceId(traceId);
        final long startTime = System.nanoTime();

        try {
            // Формируем строку параметров для логирования
            String paramsString = params.stream()
                    .map(param -> {
                        try {
                            return this.objectMapper.writeValueAsString(param);
                        } catch (Exception e) {
                            return String.valueOf(param);
                        }
                    })
                    .collect(Collectors.joining(", "));

            log.info("processing UseCase {}({}) UID {} TIME {} ms with result {}",
                    nameMethod,
                    paramsString,
                    MDC.get("X-Trace-Id"),
                    (System.nanoTime() - startTime) / 1_000_000D,
                    this.objectMapper.writeValueAsString(result)
            );
            return result;
        } catch (Exception exception) {
            // Формируем строку параметров для ошибки
            String paramsString = params.stream()
                    .map(param -> {
                        try {
                            return this.objectMapper.writeValueAsString(param);
                        } catch (Exception e) {
                            return String.valueOf(param);
                        }
                    })
                    .collect(Collectors.joining(", "));

            log.error("processing {}({}) UID {} with error {}",
                    nameMethod,
                    paramsString,
                    MDC.get("X-Trace-Id"),
                    exception.getMessage()
            );

            throw exception;
        } finally {
            clearTraceId();
        }
    }
}
