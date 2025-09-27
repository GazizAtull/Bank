package com.gaziz.bank.infrastructure.keycloak;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntrospectTokenResponse {
    @JsonProperty(value = "sub")
    private String sub;

    @JsonProperty(value = "exp")
    private long exp;

    @JsonProperty(value = "iss")
    private String iss;

    @JsonProperty(value = "email_verified")
    private boolean emailVerified;

    @JsonProperty(value = "client_id")
    private String clientId;

    @JsonProperty(value = "username")
    private String username;

    @JsonProperty(value = "active")
    private boolean active;

    @JsonProperty(value = "iin")
    private String iin;

    @JsonProperty(value = "bin")
    private String bin;
}
