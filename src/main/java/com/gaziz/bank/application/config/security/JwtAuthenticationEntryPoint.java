package com.gaziz.bank.application.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.generated.swaggerCodegen.model.BasicBackendResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

import static com.gaziz.bank.infrastructure.internal.api.handler.RestExceptionControllerAdvice.getErrorResponse;

@Component
@AllArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        BasicBackendResponse resp = getErrorResponse(authException.getMessage(), HttpStatus.UNAUTHORIZED.name());

        String jsonResponse = objectMapper.writeValueAsString(resp);
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }
}
