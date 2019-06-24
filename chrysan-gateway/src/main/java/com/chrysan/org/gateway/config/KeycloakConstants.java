package com.chrysan.org.gateway.config;

public abstract class KeycloakConstants {

    // 验证token的地址
    public static final String INTROSPECT_URI = "/realms/{realm}/protocol/openid-connect/token/introspect";

    // 登出地址
    public static final String LOGOUT_URI = "/realms/{realm}/protocol/openid-connect/logout";
}
