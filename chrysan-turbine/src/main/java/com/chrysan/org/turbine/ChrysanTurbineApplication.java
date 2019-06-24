package com.chrysan.org.turbine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

@Slf4j
@SpringBootApplication
@EnableTurbine
public class ChrysanTurbineApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(ChrysanTurbineApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }

}
