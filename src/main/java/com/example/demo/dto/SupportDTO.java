package com.example.demo.dto;

import lombok.Data;

@Data
public class SupportDTO {

    private String subject;
    private String message;
    private String successMessage;

    public static class Builder {
        private String subject;
        private String message;
        private String successMessage;

        public Builder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder successMessage(String successMessage) {
            this.successMessage = successMessage;
            return this;
        }

        public SupportDTO build() {
            SupportDTO supportDTO = new SupportDTO();
            supportDTO.subject = this.subject;
            supportDTO.message = this.message;
            supportDTO.successMessage = this.successMessage;
            return supportDTO;
        }
    }
}
