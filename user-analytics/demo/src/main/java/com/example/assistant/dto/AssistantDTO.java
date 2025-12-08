package com.example.assistant.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssistantDTO {

    @NotBlank(message = "Message text cannot be empty")
    @Size(max = 1000, message = "Message text cannot exceed 1000 characters")
    private String text;

    private String sessionId;
    private String userId;
}
