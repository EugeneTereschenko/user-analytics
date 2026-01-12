/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.patientservice.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
