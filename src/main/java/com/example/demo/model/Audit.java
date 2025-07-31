package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Entity
@Table(name = "audit")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user", length = 255)
    private String user;
    @Column(name = "action", length = 255)
    private String action;
    @Column(name = "target", length = 255)
    private String target;

    private Timestamp timestamp;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String user;
        private String action;
        private String target;
        private Timestamp timestamp;

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

        public Builder timestamp(Timestamp timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Audit build() {
            Audit audit = new Audit();
            audit.user = this.user;
            audit.action = this.action;
            audit.target = this.target;
            audit.timestamp = this.timestamp;
            return audit;
        }
    }
}
