/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.appointmentservice.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
