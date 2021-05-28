package com.chen.userauth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author chen
 */
@SpringBootApplication(scanBasePackages = {"com.chen.userauth", "com.chen.common"})
@EnableDiscoveryClient
@EnableCaching
@MapperScan("com.chen.userauth.mapper")
public class UserAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserAuthApplication.class,args);
    }
}
