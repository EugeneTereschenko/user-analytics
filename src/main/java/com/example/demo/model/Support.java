package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"support\"")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Support {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "subject", length = 255)
    private String subject;
    @Column(name = "message", length = 1000)
    private String message;
    @Column(name = "successMessage", length = 255)
    private String successMessage;


    public static Builder builder() {
        return new Builder();
    }

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

        public Support build() {
            Support support = new Support();
            support.subject = this.subject;
            support.message = this.message;
            support.successMessage = this.successMessage;
            return support;
        }
    }
}
