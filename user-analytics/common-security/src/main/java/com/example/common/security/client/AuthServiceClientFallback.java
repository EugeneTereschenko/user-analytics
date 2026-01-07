/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.common.security.client;

import com.example.common.security.dto.TokenValidationRequest;
import com.example.common.security.dto.TokenValidationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthServiceClientFallback implements AuthServiceClient {

    @Override
    public TokenValidationResponse validateToken(TokenValidationRequest request) {
        log.error("Auth service is unavailable. Fallback triggered for token validation.");
        return TokenValidationResponse.builder()
                .valid(false)
                .message("Authentication service is temporarily unavailable")
                .build();
    }

    @Override
    public TokenValidationResponse validateTokenFromHeader(String authHeader) {
        log.error("Auth service is unavailable. Fallback triggered for header validation.");
        return TokenValidationResponse.builder()
                .valid(false)
                .message("Authentication service is temporarily unavailable")
                .build();
    }
}
