package com.chrysan.org.gateway;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ChrysanGatewayApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ChrysanGatewayApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }

}
