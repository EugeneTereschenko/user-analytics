package com.example.demo.interceptor;

import com.example.activity.dto.ActivityDTO;
import com.example.activity.model.ActivityType;
import com.example.activity.service.ActivityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class ActivityLoggingInterceptor implements HandlerInterceptor {

    private final ActivityService activityService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Skip activity logging for activity endpoints to avoid infinite loops
        if (request.getRequestURI().contains("/api/activities")) {
            return true;
        }

        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
                String username = auth.getName();
                String method = request.getMethod();
                String uri = request.getRequestURI();

                ActivityType type = determineActivityType(method);
                String description = String.format("%s request to %s", method, uri);

                ActivityDTO activityDTO = new ActivityDTO.Builder()
                        .username(username)
                        .type(type)
                        .description(description)
                        .ipAddress(getClientIp(request))
                        .deviceType(request.getHeader("User-Agent"))
                        .location("Unknown")
                        .build();

                activityService.logActivity(activityDTO);
            }
        } catch (Exception e) {
            log.error("Error logging activity: {}", e.getMessage());
        }

        return true;
    }

    private ActivityType determineActivityType(String httpMethod) {
        return switch (httpMethod.toUpperCase()) {
            case "GET" -> ActivityType.VIEW;
            case "POST" -> ActivityType.CREATE;
            case "PUT", "PATCH" -> ActivityType.UPDATE;
            case "DELETE" -> ActivityType.DELETE;
            default -> ActivityType.VIEW;
        };
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
