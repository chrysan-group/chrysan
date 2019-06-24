package com.chrysan.org.hystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication
@EnableHystrixDashboard
public class ChrysanHystrixApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChrysanHystrixApplication.class, args);
	}

}
