/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.common.security.config;

import com.example.common.security.client.AuthServiceClient;
import com.example.common.security.client.AuthServiceRestClient;
import com.example.common.security.filter.JwtAccessDeniedHandler;
import com.example.common.security.filter.JwtAuthenticationEntryPoint;
import com.example.common.security.filter.JwtAuthenticationFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackages = "com.example.common.security")
@EnableFeignClients(basePackages = "com.example.common.security.client")
public class SecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
