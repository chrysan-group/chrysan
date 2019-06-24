package com.chrysan.org.gateway;

import com.alibaba.fastjson.JSONObject;
import com.chrysan.org.gateway.config.KeycloakConstants;
import com.chrysan.org.gateway.config.KeycloakProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ChrysanGatewayApplicationTests {
    @Autowired
    private KeycloakProperties keycloakProperties;

    @Test
    public void contextLoads() {
    }

    @Test
    public void introspectTest() {
        String accessToken = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJiSmkzUnVQR0dkTUhITlI0REw1dXo2R3RxeGZ6MmYyYWtobDdFRENLR2g0In0.eyJqdGkiOiJhN2JmYzU2NC1hM2EyLTQwMTktYTE2NS04NTE0ZmM0ZmVlNGQiLCJleHAiOjE1NjE3MTMwOTIsIm5iZiI6MCwiaWF0IjoxNTYxNzEyNzkyLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODAvYXV0aC9yZWFsbXMvY2hyeXNhbiIsInN1YiI6IjFlOTNhNGU3LTQxYmQtNDA1Mi1iYzRiLWRjZDUzZGJmMGViYSIsInR5cCI6IkJlYXJlciIsImF6cCI6InNlY3VyaXR5LWFkbWluLWNvbnNvbGUiLCJub25jZSI6ImNjZGY3NmUxLTBiOTUtNDBlMS04YTc3LWMzYmE3MjE3MzAyYyIsImF1dGhfdGltZSI6MTU2MTcxMjc5Miwic2Vzc2lvbl9zdGF0ZSI6ImFlMTJhYTAxLWUxNTYtNDZlNy1hMTMzLWI2NTU3YzE1MWNlYyIsImFjciI6IjEiLCJzY29wZSI6Im9wZW5pZCBlbWFpbCBwcm9maWxlIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJjaHJ5c2FuYWRtaW4ifQ.YjYEwhL-oYtrG4eyZz8s2zqDr4Ygy07bzlrq7jqsrTaYukMmZ03WUd-v0OmHgfwM971yO7547eojPnPJ-BTckTs8dtI7J8Fz1JQ3Zcgy5T97Yc-KPS3yAUizb32Et0fvp6SLNyTAZVwNChl9xP7e78FcsamqiCABH7-1BU1WDjEYGAsS5rOpmoLsGjuJ8N2cSVADHs0zIVCzj50qU8mq02pUYXooemnL3m2m7WgkCRFotexmMnJaHZUcxJ0xkTe6zsrGqqxWAGbglxu-iA3Ptv0SevWdhGkn16tO3jDFovIIQblAgXKgJq5pj_fc8eRHQ7srtZiRaBPvd-dKs3D5fw";
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("token", accessToken.replaceFirst("Bearer ", ""));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(keycloakProperties.getClientId(), keycloakProperties.getClientSecret(), StandardCharsets.UTF_8);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> response = restTemplate.postForEntity(keycloakProperties.getEndpoint() + KeycloakConstants.INTROSPECT_URI, httpEntity, JSONObject.class, keycloakProperties.getRealm());
        response.getBody().forEach((k, v) -> {
            log.info(k + ":" + v.toString());
        });
        log.info(response.getBody().toJSONString());
    }
}
