package com.gaziz.bank.domain;

import com.gaziz.bank.infrastructure.keycloak.UserInfoResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class AccountAuthDetails implements UserDetails {
    private final UserInfoResponse userInfoResponse;

    public AccountAuthDetails(final UserInfoResponse userInfoResponse) {
        this.userInfoResponse = userInfoResponse;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getUsername() {
        return this.userInfoResponse.getUsername();
    }

    public UUID getSub() {
        return UUID.fromString(this.userInfoResponse.getSub());
    }

    public UserInfoResponse getUserInfo() {
        return this.userInfoResponse;
    }
}
