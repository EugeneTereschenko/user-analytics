package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "\"calendar\"")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Calendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", length = 255)
    private String title;

    private Date date;

   public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String title;
        private Date date;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder date(Date date) {
            this.date = date;
            return this;
        }

        public Calendar build() {
            Calendar calendar = new Calendar();
            calendar.title = this.title;
            calendar.date = this.date;
            return calendar;
        }
    }
}
