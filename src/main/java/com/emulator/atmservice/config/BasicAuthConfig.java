package com.emulator.atmservice.config;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BasicAuthConfig {

    @Value("${bank.service.user}")
    private String user;

    @Value("${bank.service.password}")
    private String password;

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(user, password);
    }
}
