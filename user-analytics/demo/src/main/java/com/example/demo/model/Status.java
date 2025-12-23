package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"status\"")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(name = "uptime", length = 255)
    private String uptime;
    @Column(name = "cpu", length = 255)
    private String cpu;
    @Column(name = "memory", length = 255)
    private String memory;
    @Column(name = "api_latency", length = 255)
    private String apiLatency;
    @Column(name = "jobs", length = 255)
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

        public Status build() {
            Status status = new Status();
            status.uptime = this.uptime;
            status.cpu = this.cpu;
            status.memory = this.memory;
            status.apiLatency = this.apiLatency;
            status.jobs = this.jobs;
            return status;
        }
    }
}
