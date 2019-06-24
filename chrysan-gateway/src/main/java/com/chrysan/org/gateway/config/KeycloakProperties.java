package com.chrysan.org.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@lombok.Data
@Component
@ConfigurationProperties("keycloak")
public class KeycloakProperties {

    private String realm;
    private String clientId;
    private String clientSecret;
    private String endpoint;
}
