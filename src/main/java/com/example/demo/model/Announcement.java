package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "announcement")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Announcement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", length = 255)
    private String title;
    @Column(name = "body", length = 1000)
    private String body;
    @Column(name = "date", length = 255)
    private Date date;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String title;
        private String body;
        private Date date;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder date(Date date) {
            this.date = date;
            return this;
        }

        public Announcement build() {
            Announcement announcement = new Announcement();
            announcement.title = this.title;
            announcement.body = this.body;
            announcement.date = this.date;
            return announcement;
        }
    }
}
