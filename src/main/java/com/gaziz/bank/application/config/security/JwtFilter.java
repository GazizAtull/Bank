package com.gaziz.bank.application.config.security;

import com.gaziz.bank.application.port.in.AuthenticationUCase;
import com.gaziz.bank.infrastructure.keycloak.IntrospectTokenResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {
    public static final String AUTHORIZATION = "Authorization";
    private final AuthenticationUCase authenticateUCase;

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            final String authorization = request.getHeader(AUTHORIZATION);
            if (authorization != null) {
                final String token = authorization.replace("Bearer ", "");
                IntrospectTokenResponse introspect = authenticateUCase.introspect(token);
                UserDetails authenticate = authenticateUCase.authenticate(token, introspect);
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                authenticate,
                                null,
                                authenticate.getAuthorities()
                        )
                );
            }
        } catch (AccessDeniedException e) {
            log.error("{}", e);
        } finally {
            filterChain.doFilter(request, response);
        }
    }
}
