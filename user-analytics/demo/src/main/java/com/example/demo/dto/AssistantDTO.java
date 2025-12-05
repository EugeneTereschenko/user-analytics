package com.example.demo.dto;

import lombok.Data;

@Data
public class AssistantDTO {
    private String sender;
    private String text;

    public static class Builder {
        private String sender;
        private String text;

        public Builder sender(String sender) {
            this.sender = sender;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public AssistantDTO build() {
            AssistantDTO assistantDTO = new AssistantDTO();
            assistantDTO.sender = this.sender;
            assistantDTO.text = this.text;
            return assistantDTO;
        }
    }
}
