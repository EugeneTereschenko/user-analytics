package com.example.assistant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssistantDTO {

    @NotBlank(message = "Message text cannot be empty")
    @Size(max = 1000, message = "Message text cannot exceed 1000 characters")
    private String text;

    private String sessionId; // Optional: for conversation tracking
    private String userId; // Optional: for user-specific responses
}
