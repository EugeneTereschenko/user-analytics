# Fix for Duplicate Feign Client Bean Error

## Problem
```
BeanDefinitionOverrideException: Invalid bean definition with name 'auth-service.FeignClientSpecification'
```

This occurs when the same Feign client is registered multiple times.

## Solution 1: Quick Fix (Recommended for Development)

Add this to your `patientservice/src/main/resources/application.yml`:

```yaml
spring:
  main:
    allow-bean-definition-overriding: true
```

This allows Spring to override duplicate beans.

## Solution 2: Proper Fix (Recommended for Production)

Update your Patient Service Main Application class to be more specific about Feign client scanning:

### Before (Problematic):
```java
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {
    "com.example.common.security.client",
    "com.example.patientservice.client"
})
@ComponentScan(basePackages = {
    "com.example.common.security",
    "com.example.patientservice"
})
public class PatientServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PatientServiceApplication.class, args);
    }
}
```

### After (Fixed):
```java
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(
    basePackages = "com.example.common.security.client"
    // Remove patientservice.client if you don't have custom Feign clients
)
@ComponentScan(basePackages = {
    "com.example.common.security",
    "com.example.patientservice"
})
public class PatientServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PatientServiceApplication.class, args);
    }
}
```

## Solution 3: Use Default Scanning (Simplest)

If you don't have custom Feign clients in your patient service, simplify to:

```java
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.example.common.security.client")
public class PatientServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PatientServiceApplication.class, args);
    }
}
```

Remove the `@ComponentScan` annotation - Spring Boot will automatically scan the package and sub-packages.

## Solution 4: If You Don't Have Patient Service Feign Clients

Most likely, your patient service doesn't have its own Feign clients yet. In that case:

```java
package com.example.patientservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.example.common.security.client")
public class PatientServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(PatientServiceApplication.class, args);
    }
}
```

## Verification Steps

After applying the fix:

1. **Clean and rebuild:**
```bash
cd patientservice
mvn clean package
```

2. **Run the service:**
```bash
mvn spring-boot:run
```

3. **Check the logs:**
   You should see:
```
Started PatientServiceApplication in X.XXX seconds
```

## Why This Happens

The error occurs because:

1. **Common Security Library** has `@EnableFeignClients` in its auto-configuration
2. **Your Service** also has `@EnableFeignClients`
3. Both are scanning overlapping packages
4. The `auth-service` Feign client gets registered twice

## Additional Configuration

If you continue to have issues, add this to `application.yml`:

```yaml
spring:
  main:
    allow-bean-definition-overriding: true
  cloud:
    openfeign:
      client:
        config:
          default:
            connectTimeout: 5000
            readTimeout: 5000

feign:
  client:
    config:
      auth-service:
        connectTimeout: 5000
        readTimeout: 5000
```

## For All Other Services

Apply the same fix to:
- appointment-service
- medicalrecordservice
- doctorservice
- billingservice
- prescriptionservice

Each should have a Main Application class like:

```java
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.example.common.security.client")
public class [ServiceName]Application {
    public static void main(String[] args) {
        SpringApplication.run([ServiceName]Application.class, args);
    }
}
```