/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.medicalrecordservice.config;


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
