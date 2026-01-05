/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.common.security.aspect;

import com.example.common.security.annotation.RequirePermission;
import com.example.common.security.dto.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class PermissionAspect {

    @Around("@annotation(requirePermission)")
    public Object checkPermission(ProceedingJoinPoint joinPoint, RequirePermission requirePermission) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal)) {
            throw new AccessDeniedException("User is not authenticated");
        }

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String[] requiredPermissions = requirePermission.value();

        boolean hasPermission = switch (requirePermission.operator()) {
            case AND -> hasAllPermissions(userPrincipal, requiredPermissions);
            case OR -> hasAnyPermission(userPrincipal, requiredPermissions);
        };

        if (!hasPermission) {
            log.warn("User {} attempted to access resource without required permissions: {}",
                    userPrincipal.getUsername(), String.join(", ", requiredPermissions));
            throw new AccessDeniedException("Insufficient permissions");
        }

        return joinPoint.proceed();
    }

    private boolean hasAllPermissions(UserPrincipal userPrincipal, String[] permissions) {
        for (String permission : permissions) {
            if (!userPrincipal.hasPermission(permission)) {
                return false;
            }
        }
        return true;
    }

    private boolean hasAnyPermission(UserPrincipal userPrincipal, String[] permissions) {
        return userPrincipal.hasAnyPermission(permissions);
    }
}