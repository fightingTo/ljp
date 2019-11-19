package com.cheer.ad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 广告索引检索及搜索模块
 *
 * @EnableFeignClients ：使用fegin调用其他微服务接口
 * @EnableEurekaClient ：注册当前模块到erueka服务端
 * @EnableHystrix ：断路器
 * @EnableCircuitBreaker ：断路器
 * @EnableHystrixDashboard ：监控
 * @EnableDiscoveryClient ：发现其他微服务能力，ribbon
 *
 * @Created by ljp on 2019/11/19.
 */
@EnableFeignClients
@EnableEurekaClient
@EnableHystrix
@EnableCircuitBreaker
@EnableHystrixDashboard
@EnableDiscoveryClient
@SpringBootApplication
public class SearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchApplication.class, args);
    }
}
