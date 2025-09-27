package com.gaziz.bank.application.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("settings")
@Data
public class AppSettings {

    private Security security;

    @Data
    public static class Security {
        private String[] permitAll;
        private String clientId;
        private String clientSecret;
        private String introspectUri;
    }
}
