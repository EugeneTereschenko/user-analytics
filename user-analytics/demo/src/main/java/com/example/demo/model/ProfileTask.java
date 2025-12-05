package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profile_task")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_id", length = 255)
    private Long profileId;

    @Column(name = "task_id", length = 255)
    private Long taskId;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long profileId;
        private Long taskId;

        public Builder profileId(Long profileId) {
            this.profileId = profileId;
            return this;
        }

        public Builder taskId(Long taskId) {
            this.taskId = taskId;
            return this;
        }

        public ProfileTask build() {
            ProfileTask profileTask = new ProfileTask();
            profileTask.profileId = this.profileId;
            profileTask.taskId = this.taskId;
            return profileTask;
        }
    }
}
