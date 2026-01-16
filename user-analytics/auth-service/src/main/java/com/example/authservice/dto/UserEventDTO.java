/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.authservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEventDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private String username;
    private String email;
    private String eventType; // USER_CREATED, USER_UPDATED, USER_LOGIN

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    private List<String> roles;
    private String location;
    private String deviceType;
    private Boolean isActive;
}
