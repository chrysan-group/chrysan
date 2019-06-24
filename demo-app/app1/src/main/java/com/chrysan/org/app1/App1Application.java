package com.chrysan.org.app1;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
public class App1Application {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(App1Application.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
    // SpringBoot2.0以后，不提供 hystrix.stream节点，需要自己增加
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
@RequestMapping({"/api/get"})
class App2Controller {

    private RestTemplate restTemplate;

    public App2Controller(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public ResponseEntity testGet() {
        return ResponseEntity.ok("app1-get");
    }

    @GetMapping(path = "/{id}")
    @HystrixCommand(fallbackMethod = "findByIdFallback", commandKey = "根据ID查找对象", threadPoolKey = "APP1全局线程池")
    public ResponseEntity findById(@PathVariable int id) {
        restTemplate.put("http://APP2/api/put", null);
        return ResponseEntity.ok("findById succeed! id: " + id);
    }

    public ResponseEntity findByIdFallback(int id){
        return ResponseEntity.ok("fallback:" + id);
    }
}