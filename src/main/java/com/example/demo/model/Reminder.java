package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.util.Date;

@Entity
@Table(name = "\"reminder\"")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "date")
    private Date date;

    @Column(name = "time")
    private Time time;

    @Column(name = "notified")
    private Boolean notified;


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String title;
        private Date date;
        private Time time;
        private Boolean notified;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder date(Date date) {
            this.date = date;
            return this;
        }

        public Builder time(Time time) {
            this.time = time;
            return this;
        }

        public Builder notified(Boolean notified) {
            this.notified = notified;
            return this;
        }

        public Reminder build() {
            Reminder reminder = new Reminder();
            reminder.title = this.title;
            reminder.date = this.date;
            reminder.time = this.time;
            reminder.notified = this.notified;
            return reminder;
        }
    }
}
