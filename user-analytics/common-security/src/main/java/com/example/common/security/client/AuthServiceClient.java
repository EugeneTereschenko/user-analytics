/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.common.security.client;

import com.example.common.security.dto.TokenValidationRequest;
import com.example.common.security.dto.TokenValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "auth-service",
        path = "/api/auth",
        fallback = AuthServiceClientFallback.class
)
public interface AuthServiceClient {

    @PostMapping("/validate")
    TokenValidationResponse validateToken(@RequestBody TokenValidationRequest request);

    @PostMapping("/validate-header")
    TokenValidationResponse validateTokenFromHeader(@RequestHeader("Authorization") String authHeader);
}
