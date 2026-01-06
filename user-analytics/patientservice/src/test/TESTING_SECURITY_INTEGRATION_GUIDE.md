# Testing with Security Integration Guide

## Problem

When using `@RequirePermission` annotations from the common security library, tests fail with:

```
AccessDeniedException: User is not authenticated
Status expected:<201> but was:<500>
```

This happens because `@WithMockUser` creates a standard Spring Security `User`, but the security library expects a custom `UserPrincipal` object.

## Solution Options

### Option 1: Disable Security in Tests (Recommended for Integration Tests)

**When to use:** Testing business logic without security concerns.

#### Step 1: Create TestSecurityConfig

Create `src/test/java/com/healthcare/patientservice/config/TestSecurityConfig.java`:

```java
package com.healthcare.patientservice.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
@Profile("test")
public class TestSecurityConfig {

    @Bean
    @Primary
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}
```

#### Step 2: Update Your Test Class

```java
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import({TestcontainersConfiguration.class, TestSecurityConfig.class}) // ✨ Add TestSecurityConfig
@Transactional
class PatientControllerTest {

    @Test
    void createPatient_Success() throws Exception {
        // No @WithMockUser needed - security is disabled
        mockMvc.perform(post("/api/v1/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientDTO)))
            .andExpect(status().isCreated());
    }
}
```

#### Step 3: Run Tests

```bash
mvn test
```

**Pros:**
- ✅ Simple and fast
- ✅ Focuses on business logic
- ✅ No security boilerplate in tests

**Cons:**
- ❌ Doesn't test security rules
- ❌ May miss security-related bugs

---

### Option 2: Custom @WithMockUserPrincipal Annotation (Recommended for Security Tests)

**When to use:** Testing permission-based access control.

#### Step 1: Create Custom Annotation

Create `src/test/java/com/healthcare/patientservice/config/WithMockUserPrincipal.java`:

```java
package com.healthcare.patientservice.config;

import org.springframework.security.test.context.support.WithSecurityContext;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockUserPrincipalSecurityContextFactory.class)
public @interface WithMockUserPrincipal {
    long userId() default 1L;
    String username() default "testuser";
    String email() default "test@example.com";
    String userType() default "ADMIN";
    String[] roles() default {"ROLE_ADMIN"};
    String[] permissions() default {
        "PATIENT_CREATE", "PATIENT_READ", "PATIENT_UPDATE", "PATIENT_DELETE"
    };
}
```

#### Step 2: Create Security Context Factory

Create `src/test/java/com/healthcare/patientservice/config/WithMockUserPrincipalSecurityContextFactory.java`:

```java
package com.healthcare.patientservice.config;

import com.example.common.security.dto.UserPrincipal;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import java.util.Set;

public class WithMockUserPrincipalSecurityContextFactory 
        implements WithSecurityContextFactory<WithMockUserPrincipal> {

    @Override
    public SecurityContext createSecurityContext(WithMockUserPrincipal annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserPrincipal principal = UserPrincipal.builder()
                .userId(annotation.userId())
                .username(annotation.username())
                .email(annotation.email())
                .userType(annotation.userType())
                .roles(Set.of(annotation.roles()))
                .permissions(Set.of(annotation.permissions()))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();

        Authentication auth = new UsernamePasswordAuthenticationToken(
                principal, null, principal.getAuthorities());

        context.setAuthentication(auth);
        return context;
    }
}
```

#### Step 3: Use in Tests

```java
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class PatientControllerSecurityTest {

    @Test
    @WithMockUserPrincipal(
        username = "admin",
        roles = {"ROLE_ADMIN"},
        permissions = {"PATIENT_CREATE", "PATIENT_READ"}
    )
    void createPatient_AsAdmin_Success() throws Exception {
        mockMvc.perform(post("/api/v1/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientDTO)))
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUserPrincipal(
        username = "patient",
        roles = {"ROLE_PATIENT"},
        permissions = {"APPOINTMENT_READ"} // No PATIENT_CREATE
    )
    void createPatient_AsPatient_Forbidden() throws Exception {
        mockMvc.perform(post("/api/v1/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientDTO)))
            .andExpect(status().isForbidden()); // 403
    }
}
```

**Pros:**
- ✅ Tests actual security rules
- ✅ Validates permissions and roles
- ✅ Can test different user types

**Cons:**
- ❌ More setup required
- ❌ Tests run slower
- ❌ More complex test code

---

### Option 3: Disable Security Filter in Specific Tests

**When to use:** Quick fix for existing tests.

```java
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // ✨ Disable all filters
@ActiveProfiles("test")
class PatientControllerTest {
    // Tests run without security filters
}
```

**Pros:**
- ✅ Quick one-line fix
- ✅ No additional configuration needed

**Cons:**
- ❌ Disables ALL filters (not just security)
- ❌ May hide other issues

---

### Option 4: Mock the Auth Service

**When to use:** Testing with actual JWT validation flow.

```java
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PatientControllerTest {

    @MockBean
    private AuthServiceClient authServiceClient;

    @BeforeEach
    void setup() {
        TokenValidationResponse mockResponse = TokenValidationResponse.builder()
                .valid(true)
                .userId(1L)
                .username("testuser")
                .roles(Set.of("ROLE_ADMIN"))
                .permissions(Set.of("PATIENT_CREATE", "PATIENT_READ"))
                .build();

        when(authServiceClient.validateTokenFromHeader(anyString()))
                .thenReturn(mockResponse);
    }

    @Test
    void createPatient_Success() throws Exception {
        mockMvc.perform(post("/api/v1/patients")
                .header("Authorization", "Bearer fake-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientDTO)))
            .andExpect(status().isCreated());
    }
}
```

---

## Comparison Matrix

| Approach | Speed | Security Testing | Complexity | Best For |
|----------|-------|------------------|------------|----------|
| **Disabled Security** | ⚡⚡⚡ Fast | ❌ No | 🟢 Low | Integration tests |
| **@WithMockUserPrincipal** | ⚡⚡ Medium | ✅ Yes | 🟡 Medium | Security tests |
| **addFilters = false** | ⚡⚡⚡ Fast | ❌ No | 🟢 Low | Quick fixes |
| **Mock Auth Service** | ⚡ Slow | ✅ Partial | 🔴 High | E2E tests |

---

## Recommended Test Structure

```
src/test/java/
├── config/
│   ├── TestSecurityConfig.java           # Disables security for integration tests
│   ├── WithMockUserPrincipal.java        # Custom annotation
│   └── WithMockUserPrincipalSecurityContextFactory.java
├── controller/
│   ├── PatientControllerTest.java        # Integration tests (security disabled)
│   └── PatientControllerSecurityTest.java # Security tests (with @WithMockUserPrincipal)
└── service/
    └── PatientServiceTest.java           # Unit tests
```

---

## Complete Example: Updated Test Class

```java
package com.healthcare.patientservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.patientservice.config.TestSecurityConfig;
import com.healthcare.patientservice.dto.PatientDTO;
import com.healthcare.patientservice.entity.Gender;
import com.healthcare.patientservice.entity.PatientStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class) // ✨ This is the key change
@Transactional
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createPatient_Success() throws Exception {
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setEmail("john.doe@example.com");
        patientDTO.setFirstName("John");
        patientDTO.setLastName("Doe");
        patientDTO.setStatus(PatientStatus.ACTIVE);
        patientDTO.setPhoneNumber("1234567890");
        patientDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patientDTO.setGender(Gender.MALE);

        mockMvc.perform(post("/api/v1/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }
}
```

---

## Quick Fix for Your Current Tests

**Add this one class and one line to your test:**

1. **Create** `TestSecurityConfig.java` (see code above)
2. **Update** your test class:
   ```java
   @Import({TestcontainersConfiguration.class, TestSecurityConfig.class}) // ✨ Add this
   ```
3. **Remove** `@WithMockUser` from all test methods
4. **Run** tests

---

## Troubleshooting

### Tests Still Failing?

1. **Check profile is active:**
   ```java
   @ActiveProfiles("test")
   ```

2. **Verify TestSecurityConfig is imported:**
   ```java
   @Import(TestSecurityConfig.class)
   ```

3. **Check application-test.yml exists:**
    - Location: `src/test/resources/application-test.yml`

4. **Enable debug logging:**
   ```yaml
   logging:
     level:
       org.springframework.security: DEBUG
   ```

### Common Errors

**Error: "No bean named 'testSecurityFilterChain'"**
- Solution: Add `@Primary` to the bean in `TestSecurityConfig`

**Error: "Multiple SecurityFilterChain beans"**
- Solution: Use `@Primary` annotation or `@Profile("test")`

**Error: "AuthServiceClient not found"**
- Solution: Add `@MockBean` for the client or disable Feign in tests

---

## Best Practices

1. **Separate Security Tests**
    - Use `*Test.java` for business logic (security disabled)
    - Use `*SecurityTest.java` for security rules (with @WithMockUserPrincipal)

2. **Use Test Profiles**
    - Always use `@ActiveProfiles("test")`
    - Configure test-specific properties in `application-test.yml`

3. **Mock External Services**
    - Mock `AuthServiceClient` to avoid calling actual auth service
    - Use `@MockBean` for external dependencies

4. **Keep Tests Fast**
    - Use in-memory database (H2) or Testcontainers
    - Disable unnecessary auto-configurations
    - Use `@Transactional` for automatic rollback

5. **Test What Matters**
    - Integration tests: Focus on business logic
    - Security tests: Focus on access control
    - Don't test Spring Security framework itself

---

## Summary

**For your immediate fix:**

1. Create `TestSecurityConfig.java` with security disabled
2. Add `@Import(TestSecurityConfig.class)` to your test
3. Remove `@WithMockUser` annotations
4. Run tests - they should pass! ✅

**For comprehensive security testing:**

1. Create `@WithMockUserPrincipal` annotation
2. Create separate security test class
3. Test different roles and permissions
4. Verify access control works correctly

Choose the approach that fits your testing needs! 🎯