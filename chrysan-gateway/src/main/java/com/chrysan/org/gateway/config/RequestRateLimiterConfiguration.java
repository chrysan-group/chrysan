package com.chrysan.org.gateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class RequestRateLimiterConfiguration {
    @Bean(name = "userKeyResolver")
    KeyResolver userKeyResolver() {
        return exchange -> {
            String sub = exchange.getRequest().getHeaders().getFirst("sub");
            log.info("sub: {}", sub);
            return Mono.just(sub);
        };
    }
}
