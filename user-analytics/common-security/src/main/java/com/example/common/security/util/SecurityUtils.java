/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.common.security.util;

import com.example.common.security.dto.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Slf4j
public class SecurityUtils {

    private SecurityUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Get the currently authenticated user
     */
    public static Optional<UserPrincipal> getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
                return Optional.of((UserPrincipal) authentication.getPrincipal());
            }
        } catch (Exception e) {
            log.error("Error getting current user", e);
        }
        return Optional.empty();
    }

    /**
     * Get the current user ID
     */
    public static Optional<Long> getCurrentUserId() {
        return getCurrentUser().map(UserPrincipal::getUserId);
    }

    /**
     * Get the current username
     */
    public static Optional<String> getCurrentUsername() {
        return getCurrentUser().map(UserPrincipal::getUsername);
    }

    /**
     * Check if the current user has a specific role
     */
    public static boolean hasRole(String role) {
        return getCurrentUser()
                .map(user -> user.hasRole(role))
                .orElse(false);
    }

    /**
     * Check if the current user has any of the specified roles
     */
    public static boolean hasAnyRole(String... roles) {
        return getCurrentUser()
                .map(user -> user.hasAnyRole(roles))
                .orElse(false);
    }

    /**
     * Check if the current user has a specific permission
     */
    public static boolean hasPermission(String permission) {
        return getCurrentUser()
                .map(user -> user.hasPermission(permission))
                .orElse(false);
    }

    /**
     * Check if the current user has any of the specified permissions
     */
    public static boolean hasAnyPermission(String... permissions) {
        return getCurrentUser()
                .map(user -> user.hasAnyPermission(permissions))
                .orElse(false);
    }

    /**
     * Check if the current user is the owner of a resource
     */
    public static boolean isOwner(Long resourceOwnerId) {
        return getCurrentUserId()
                .map(userId -> userId.equals(resourceOwnerId))
                .orElse(false);
    }

    /**
     * Check if the current user is an admin
     */
    public static boolean isAdmin() {
        return hasRole("ROLE_ADMIN");
    }

    /**
     * Check if the current user is a doctor
     */
    public static boolean isDoctor() {
        return hasRole("ROLE_DOCTOR");
    }

    /**
     * Check if the current user is a patient
     */
    public static boolean isPatient() {
        return hasRole("ROLE_PATIENT");
    }

    /**
     * Get the current user's type
     */
    public static Optional<String> getCurrentUserType() {
        return getCurrentUser().map(UserPrincipal::getUserType);
    }
}
