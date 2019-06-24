package com.chrysan.org.app2;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
public class App2Application {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(App2Application.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }

    @Bean
    public ServletRegistrationBean getServlet(){
        HystrixMetricsStreamServlet streamServlet = new HystrixMetricsStreamServlet();
        ServletRegistrationBean registrationBean = new ServletRegistrationBean(streamServlet);
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/actuator/hystrix.stream");
        registrationBean.setName("HystrixMetricsStreamServlet");
        return registrationBean;
    }
}


@RestController
@RequestMapping({"/api/put"})
class App2Controller {

    @PutMapping
    @HystrixCommand(fallbackMethod = "testPutFallback", commandKey = "测试PUT请求", threadPoolKey = "APP2全局线程池")
    public ResponseEntity testPut() {
        double rate = Math.random();
        if(rate > 0.5) {
            throw new IllegalArgumentException();
        }
        return ResponseEntity.ok("app2-put");
    }

    private ResponseEntity testPutFallback() {
        return ResponseEntity.ok("app2-put-fallback");
    }
}