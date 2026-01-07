/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.appointmentservice;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {
        "com.example.common.security.client"
})
@ComponentScan(basePackages = {
        "com.example.common.security",
        "com.healthcare.appointmentservice",
})
public class AppointmentServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppointmentServiceApplication.class, args);
    }
}
