/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.common.security.constants;

public final class PermissionConstants {

    private PermissionConstants() {
        throw new IllegalStateException("Constants class");
    }

    // Patient permissions
    public static final String PATIENT_CREATE = "PATIENT_CREATE";
    public static final String PATIENT_READ = "PATIENT_READ";
    public static final String PATIENT_UPDATE = "PATIENT_UPDATE";
    public static final String PATIENT_DELETE = "PATIENT_DELETE";

    // Appointment permissions
    public static final String APPOINTMENT_CREATE = "APPOINTMENT_CREATE";
    public static final String APPOINTMENT_READ = "APPOINTMENT_READ";
    public static final String APPOINTMENT_UPDATE = "APPOINTMENT_UPDATE";
    public static final String APPOINTMENT_DELETE = "APPOINTMENT_DELETE";

    // Medical Record permissions
    public static final String MEDICAL_RECORD_CREATE = "MEDICAL_RECORD_CREATE";
    public static final String MEDICAL_RECORD_READ = "MEDICAL_RECORD_READ";
    public static final String MEDICAL_RECORD_UPDATE = "MEDICAL_RECORD_UPDATE";
    public static final String MEDICAL_RECORD_DELETE = "MEDICAL_RECORD_DELETE";

    // Prescription permissions
    public static final String PRESCRIPTION_CREATE = "PRESCRIPTION_CREATE";
    public static final String PRESCRIPTION_READ = "PRESCRIPTION_READ";
    public static final String PRESCRIPTION_UPDATE = "PRESCRIPTION_UPDATE";
    public static final String PRESCRIPTION_DELETE = "PRESCRIPTION_DELETE";

    // Billing permissions
    public static final String BILLING_CREATE = "BILLING_CREATE";
    public static final String BILLING_READ = "BILLING_READ";
    public static final String BILLING_UPDATE = "BILLING_UPDATE";
    public static final String BILLING_DELETE = "BILLING_DELETE";

    // User management permissions
    public static final String USER_CREATE = "USER_CREATE";
    public static final String USER_READ = "USER_READ";
    public static final String USER_UPDATE = "USER_UPDATE";
    public static final String USER_DELETE = "USER_DELETE";
}