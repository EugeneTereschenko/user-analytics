package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@ComponentScan(basePackages = {
        "com.example.assistant.controller",
        "com.example.assistant.service",
        "com.example.assistant",
        "com.example.demo.controller",
        "com.example.demo.service",
        "com.example.demo.repository",
        "com.example.demo.security",
        "com.example.demo"
})
public class UserAnalyticsJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserAnalyticsJavaApplication.class, args);
	}

}
