package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EntityScan({"com.example.activity.model", "com.example.demo.model"})
@ComponentScan(basePackages = {
        "com.example.assistant.controller",
        "com.example.assistant.service",
        "com.example.assistant",
        "com.example.activity.controller",
        "com.example.activity.service",
        "com.example.activity.model",
        "com.example.activity",
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
