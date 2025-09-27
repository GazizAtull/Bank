package com.gaziz.bank.domain;

import com.gaziz.bank.infrastructure.keycloak.UserInfoResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public class AccountUserDetails extends AccountAuthDetails {
    public static final String ROLE = "ROLE_USER";

    public AccountUserDetails(UserInfoResponse userInfoResponse) {
        super(userInfoResponse);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(ROLE));
    }
}
