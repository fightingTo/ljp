package com.cheer.ad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 全量数据导出服务
 *
 * @Created by ljp on 2019/11/20.
 */
@SpringBootApplication
@EnableEurekaClient
public class ExportApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExportApplication.class, args);
    }
}
