package com.chen.wcloauth2clientdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * @author chen
 */

@SpringBootApplication
@EnableWebSecurity
public class WclOauth2ClientDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WclOauth2ClientDemoApplication.class, args);
    }

}
