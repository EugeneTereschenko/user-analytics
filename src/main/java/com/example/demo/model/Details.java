package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"details\"")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Details {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "notification", length = 255)
    private Boolean notification;

    @Column(name = "staff", length = 255)
    private String staff;

    @Column(name = "bio", length = 255)
    private String bio;

    @Column(name = "message", length = 255)
    private String message;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Boolean notification;
        private String staff;
        private String bio;
        private String message;

        public Builder notification(Boolean notification) {
            this.notification = notification;
            return this;
        }

        public Builder staff(String staff) {
            this.staff = staff;
            return this;
        }

        public Builder bio(String bio) {
            this.bio = bio;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Details build() {
            Details details = new Details();
            details.notification = this.notification;
            details.staff = this.staff;
            details.bio = this.bio;
            details.message = this.message;
            return details;
        }
    }
}
