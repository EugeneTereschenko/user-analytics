package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"task\"")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(name = "title", length = 255)
    private String title;
    @Column(name = "done", length = 255)
    private String done;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String title;
        private String done;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder done(String done) {
            this.done = done;
            return this;
        }

        public Task build() {
            Task task = new Task();
            task.title = this.title;
            task.done = this.done;
            return task;
        }
    }
}
