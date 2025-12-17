package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaAuditing
@EntityScan({"com.example.activity.model",
        "com.example.demo.model",
        "com.example.announcement.model",
        "com.example.notification.model",
		"com.example.assistant.model",
		"com.example.featureusage.model",
		"com.example.report.model"})
@EnableJpaRepositories(basePackages = {
		"com.example.activity.repository",
		"com.example.announcement.repository",
		"com.example.notification.repository",
		"com.example.assistant.repository",
		"com.example.featureusage.repository",
		"com.example.demo.repository",
		"com.example.report.repository"
})
@ComponentScan(basePackages = {
		"com.example.featureusage.controller",
		"com.example.featureusage.service",
		"com.example.featureusage",
        "com.example.announcement.controller",
        "com.example.announcement.service",
        "com.example.announcement",
        "com.example.assistant.controller",
        "com.example.assistant.service",
        "com.example.assistant",
        "com.example.activity.controller",
        "com.example.activity.service",
        "com.example.activity.model",
        "com.example.activity",
        "com.example.notification.controller",
        "com.example.notification.service",
        "com.example.notification",
		"com.example.report.controller",
		"com.example.report.service",
		"com.example.report",
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
