package com.example.assistant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssistantResponseDTO {
    private String text;
    private String type; // "success", "error", "info"
    private Object data;
    private Long timestamp;

    public static AssistantResponseDTO success(String message) {
        return AssistantResponseDTO.builder()
                .text(message)
                .type("success")
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static AssistantResponseDTO error(String message) {
        return AssistantResponseDTO.builder()
                .text(message)
                .type("error")
                .timestamp(System.currentTimeMillis())
                .build();
    }
}
