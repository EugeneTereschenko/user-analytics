/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.common.security.client;
import com.example.common.security.dto.TokenValidationRequest;
import com.example.common.security.dto.TokenValidationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthServiceRestClient {

    private final RestTemplate restTemplate;

    @Value("${auth.service.url:http://auth-service:8087}")
    private String authServiceUrl;

    public TokenValidationResponse validateToken(String token) {
        try {
            String url = authServiceUrl + "/api/auth/validate";
            TokenValidationRequest request = new TokenValidationRequest(token);

            ResponseEntity<TokenValidationResponse> response = restTemplate.postForEntity(
                    url,
                    request,
                    TokenValidationResponse.class
            );

            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to validate token: {}", e.getMessage());
            return TokenValidationResponse.builder()
                    .valid(false)
                    .message("Token validation failed: " + e.getMessage())
                    .build();
        }
    }

    public TokenValidationResponse validateTokenFromHeader(String authHeader) {
        try {
            String url = authServiceUrl + "/api/auth/validate-header";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", authHeader);
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<TokenValidationResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    TokenValidationResponse.class
            );

            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to validate token from header: {}", e.getMessage());
            return TokenValidationResponse.builder()
                    .valid(false)
                    .message("Token validation failed: " + e.getMessage())
                    .build();
        }
    }
}
