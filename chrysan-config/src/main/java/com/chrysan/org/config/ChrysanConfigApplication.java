package com.chrysan.org.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

@Slf4j
@EnableDiscoveryClient
@EnableConfigServer
@SpringBootApplication
public class ChrysanConfigApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ChrysanConfigApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }

}
