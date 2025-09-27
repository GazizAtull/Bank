package com.gaziz.bank.shared.mdc;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;
/**
 * Глобальный фильтр для добавления контекстной информации в MDC (Mapped Diagnostic Context).
 *
 * <p>Этот фильтр автоматически добавляет следующую информацию к каждому HTTP запросу:</p>
 * <ul>
 *   <li><strong>X-Trace-Id</strong> - уникальный идентификатор для отслеживания запроса</li>
 *   <li><strong>remoteAddr</strong> - IP адрес клиента</li>
 *   <li><strong>userAgent</strong> - User-Agent заголовок браузера</li>
 *   <li><strong>username</strong> - имя аутентифицированного пользователя (если доступно)</li>
 * </ul>
 */
@Component
public class GlobalMDCFilter implements Filter {
    //TODO добавить информацию из principal если она есть + добавить operationId из openapi контракта
    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // Добавляем глобальный контекст
        MDC.put("X-Trace-Id", UUID.randomUUID().toString());
        MDC.put("remoteAddr", httpRequest.getRemoteAddr());
        MDC.put("userAgent", httpRequest.getHeader("User-Agent"));

//        Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
//                .ifPresent(authentication -> {
//                    MDC.put("username", authentication.getName());
//                });
        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear(); // Критически важно!
        }
    }
}
