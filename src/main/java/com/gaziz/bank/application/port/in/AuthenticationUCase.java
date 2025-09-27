package com.gaziz.bank.application.port.in;

import com.gaziz.bank.infrastructure.keycloak.IntrospectTokenResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationUCase {
    UserDetails authenticate(final String token, final IntrospectTokenResponse introspectTokenResponse);

    IntrospectTokenResponse introspect(final String token) throws AccessDeniedException;
}

