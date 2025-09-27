package com.gaziz.bank.application.port.ucase;

import com.gaziz.bank.application.port.in.AuthenticationUCase;
import com.gaziz.bank.application.props.AppSettings;
import com.gaziz.bank.domain.AccountUserDetails;
import com.gaziz.bank.infrastructure.keycloak.IntrospectTokenRequest;
import com.gaziz.bank.infrastructure.keycloak.IntrospectTokenResponse;
import com.gaziz.bank.infrastructure.keycloak.KeycloakClient;
import com.gaziz.bank.infrastructure.keycloak.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthenticationUCaseImpl implements AuthenticationUCase {
    private final KeycloakClient keycloakClient;
    private final AppSettings appSettings;

    @Override
    public UserDetails authenticate(final String token, final IntrospectTokenResponse introspect) {
        UserDetails resp;
        try {
            if (introspect.getUsername().startsWith("service-account-")) {
                resp = new AccountUserDetails(
                        new UserInfoResponse(
                                introspect.getSub(),
                                introspect.getUsername(),
                                null,
                                null,
                                null,
                                new ArrayList<>(),
                                new ArrayList<>(),
                                introspect.getIin(),
                                introspect.getBin()
                        )
                );
            } else {
                UserInfoResponse userInfo = keycloakClient.getUserInfo("Bearer " + token);
                resp = new AccountUserDetails(userInfo);
            }
        } catch (Exception e) {
            throw new AccessDeniedException(String.format("invalid token: %s", e.getMessage()), e);
        }
        return resp;
    }

    @Override
    public IntrospectTokenResponse introspect(final String token) throws AccessDeniedException {
        IntrospectTokenResponse introspect = keycloakClient.introspect(new IntrospectTokenRequest(appSettings, token));
        if (!introspect.isActive()) {
            throw new AccessDeniedException("Access denied. introspect is false");
        }
        return introspect;
    }
}
