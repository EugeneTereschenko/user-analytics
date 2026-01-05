/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.common.security.constants;

public final class RoleConstants {

    private RoleConstants() {
        throw new IllegalStateException("Constants class");
    }

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_DOCTOR = "ROLE_DOCTOR";
    public static final String ROLE_PATIENT = "ROLE_PATIENT";
    public static final String ROLE_STAFF = "ROLE_STAFF";
    public static final String ROLE_PHARMACIST = "ROLE_PHARMACIST";
    public static final String ROLE_RECEPTIONIST = "ROLE_RECEPTIONIST";
}