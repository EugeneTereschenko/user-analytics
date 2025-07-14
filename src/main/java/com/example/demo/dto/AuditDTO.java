package com.example.demo.dto;

import lombok.Data;

@Data
public class AuditDTO {
    private String user;
    private String action;
    private String target;
    private String timestamp;

    public static class Builder {
        private String user;
        private String action;
        private String target;
        private String timestamp;

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Builder action(String action) {
            this.action = action;
            return this;
        }

        public Builder target(String target) {
            this.target = target;
            return this;
        }

        public Builder timestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public AuditDTO build() {
            AuditDTO auditDTO = new AuditDTO();
            auditDTO.user = this.user;
            auditDTO.action = this.action;
            auditDTO.target = this.target;
            auditDTO.timestamp = this.timestamp;
            return auditDTO;
        }
    }
}
