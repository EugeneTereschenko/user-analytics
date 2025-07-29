package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "\"notification\"")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 255)
    public String title;

    @Column(name = "message", length = 1000)
    public String message;

    public Timestamp timestamp;


    public static Details.Builder builder() {
        return new Details.Builder();
    }

    public static class Builder {
        private String title;
        private String message;
        private Timestamp timestamp;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder timestamp(Timestamp timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Notification build() {
            Notification notification = new Notification();
            notification.title = this.title;
            notification.message = this.message;
            notification.timestamp = this.timestamp;
            return notification;
        }
    }
}
