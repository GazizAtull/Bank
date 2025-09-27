package com.gaziz.bank.infrastructure.keycloak;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoResponse {

    @JsonProperty(value = "sub")
    private String sub;

    @JsonProperty(value = "preferred_username")
    private String username;

    @JsonProperty(value = "given_name")
    private String firstName;

    @JsonProperty(value = "family_name")
    private String lastName;

    @JsonProperty(value = "email")
    private String email;

    @JsonProperty(value = "groups")
    private List<String> groups;

    @JsonProperty(value = "realm_roles")
    private List<String> roles;

    @JsonProperty(value = "iin")
    private String iin;

    @JsonProperty(value = "bin")
    private String bin;
}
