package com.gaziz.bank.infrastructure.internal.api.handler;

import com.gaziz.bank.shared.exception.NotFoundException;
import com.generated.swaggerCodegen.model.BasicBackendResponse;
import com.generated.swaggerCodegen.model.MetaData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;
import java.util.Optional;


@ControllerAdvice
@Slf4j
public class RestExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {
            NotFoundException.class
    })
    protected ResponseEntity<BasicBackendResponse> notFoundError(NotFoundException ex, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        getErrorResponse(
                                ex.getMessage(),
                                HttpStatus.NOT_FOUND.name()
                        )
                );
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        getErrorResponse(
                                ex.getMessage(),
                                HttpStatus.BAD_REQUEST.name()
                        )
                );
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        getErrorResponse(
                                ex.getMessage(),
                                HttpStatus.BAD_REQUEST.name()
                        )
                );
    }

    @ExceptionHandler(value = {
            RuntimeException.class
    })
    protected ResponseEntity<BasicBackendResponse> handleConflictInternalError(Throwable ex, WebRequest request) {
        log.error("{}", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        getErrorResponse(
                                "Oops! Неизвестная проблема",
                                HttpStatus.INTERNAL_SERVER_ERROR.name()
                        )
                );
    }

    @Override
    protected ResponseEntity<Object> handleNoResourceFoundException(
            NoResourceFoundException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        log.error("{}", ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        getErrorResponse(
                                ex.getMessage(),
                                HttpStatus.BAD_REQUEST.name()
                        )
                );
    }


    public static BasicBackendResponse getErrorResponse(final String msg, final String errorCode) {
        BasicBackendResponse response = new BasicBackendResponse();
        MetaData meta = new MetaData();
        meta.setTimestamp(Instant.now().toString());

        response.setMeta(meta);
        response.setErrorCode(errorCode);
        response.setDescription(msg);
        return response;
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BasicBackendResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        getErrorResponse(
                                ex.getMessage(),
                                HttpStatus.BAD_REQUEST.name()
                        )
                );
    }

}

