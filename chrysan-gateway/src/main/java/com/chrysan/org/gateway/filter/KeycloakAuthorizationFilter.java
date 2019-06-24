package com.chrysan.org.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.chrysan.org.gateway.config.KeycloakConstants;
import com.chrysan.org.gateway.config.KeycloakProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.gateway.config.HttpClientProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.NettyRoutingFilter;
import org.springframework.cloud.gateway.filter.headers.HttpHeadersFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.nio.charset.StandardCharsets;
import java.util.List;


@Slf4j
@Component
public class KeycloakAuthorizationFilter extends NettyRoutingFilter {

    private RestTemplate restTemplate;

    private KeycloakProperties keycloakProperties;

    public KeycloakAuthorizationFilter(HttpClient httpClient,
                                       ObjectProvider<List<HttpHeadersFilter>> headersFiltersProvider,
                                       HttpClientProperties properties,
                                       KeycloakProperties keycloakProperties) {
        super(httpClient, headersFiltersProvider, properties);
        this.restTemplate = new RestTemplate();
        this.keycloakProperties = keycloakProperties;
    }


    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String kcToken = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (kcToken == null || kcToken.length() == 0 || !kcToken.startsWith("Bearer ")) {
            ServerHttpResponse serverHttpResponse = exchange.getResponse();
            serverHttpResponse.setStatusCode(HttpStatus.BAD_REQUEST);
            serverHttpResponse.getHeaders().remove(HttpHeaders.CONTENT_LENGTH);
            return serverHttpResponse.setComplete();

        } else {
            JSONObject idToken = introspect(keycloakProperties.getEndpoint() + KeycloakConstants.INTROSPECT_URI,
                    keycloakProperties.getRealm(),
                    keycloakProperties.getClientId(),
                    keycloakProperties.getClientSecret(),
                    kcToken);
            log.debug("#####################");
            log.debug(idToken.toJSONString());
            log.debug("#####################");
            if (!idToken.getBoolean("active")) {
                ServerHttpResponse serverHttpResponse = exchange.getResponse();
                serverHttpResponse.setStatusCode(HttpStatus.UNAUTHORIZED);
                serverHttpResponse.getHeaders().remove(HttpHeaders.CONTENT_LENGTH);
                return serverHttpResponse.setComplete();
            }
            ServerHttpRequest request = exchange.getRequest().mutate()
                    .header("sub", idToken.getString("sub")).build();

            return chain.filter(exchange.mutate().request(request).build());
        }

    }

    private JSONObject introspect(String url, String realm, String clientId, String clientSecret, String accessToken) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("token", accessToken.replaceFirst("Bearer ", ""));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret, StandardCharsets.UTF_8);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, headers);
        return restTemplate.postForObject(url, httpEntity, JSONObject.class, realm);
    }
}
