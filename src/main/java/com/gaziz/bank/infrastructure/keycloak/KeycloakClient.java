package com.gaziz.bank.infrastructure.keycloak;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "keycloak", url = "${settings.security.host}")
public interface KeycloakClient {

    @PostMapping(value = "${settings.security.introspect-uri}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    IntrospectTokenResponse introspect(@RequestBody final IntrospectTokenRequest request);

    @GetMapping(value = "${settings.security.user-info-uri}")
    UserInfoResponse getUserInfo(@RequestHeader("Authorization") final String bearerToken);
}
