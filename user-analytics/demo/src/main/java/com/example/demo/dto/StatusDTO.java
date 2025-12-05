package com.example.demo.dto;

import lombok.Data;

@Data
public class StatusDTO {
    private String uptime;
    private String cpu;
    private String memory;
    private String apiLatency;
    private String jobs;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String uptime;
        private String cpu;
        private String memory;
        private String apiLatency;
        private String jobs;

        public Builder uptime(String uptime) {
            this.uptime = uptime;
            return this;
        }

        public Builder cpu(String cpu) {
            this.cpu = cpu;
            return this;
        }

        public Builder memory(String memory) {
            this.memory = memory;
            return this;
        }

        public Builder apiLatency(String apiLatency) {
            this.apiLatency = apiLatency;
            return this;
        }

        public Builder jobs(String jobs) {
            this.jobs = jobs;
            return this;
        }

        public StatusDTO build() {
            StatusDTO statusDTO = new StatusDTO();
            statusDTO.uptime = this.uptime;
            statusDTO.cpu = this.cpu;
            statusDTO.memory = this.memory;
            statusDTO.apiLatency = this.apiLatency;
            statusDTO.jobs = this.jobs;
            return statusDTO;
        }
    }
}
