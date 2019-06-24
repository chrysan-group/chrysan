package com.chrysan.org.eureka;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


@EnableEurekaServer
@SpringBootApplication
public class ChrysanEurekaApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ChrysanEurekaApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }

}
