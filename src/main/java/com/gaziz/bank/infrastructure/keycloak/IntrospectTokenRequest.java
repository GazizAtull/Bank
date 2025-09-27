package com.gaziz.bank.infrastructure.keycloak;

import com.gaziz.bank.application.props.AppSettings;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IntrospectTokenRequest {
    private String token;
    private String client_id;
    private String client_secret;
    private String grant_type;

    public IntrospectTokenRequest(final AppSettings settings, final String token) {
        this.client_id = settings.getSecurity().getClientId();
        this.client_secret = settings.getSecurity().getClientSecret();
        this.token = token;
        this.grant_type = "client_credentials";
    }
}
