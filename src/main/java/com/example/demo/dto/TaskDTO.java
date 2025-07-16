package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TaskDTO {
    private String id;
    private String title;
    private String done;

    public static class Builder {
        private String id;
        private String title;
        private String done;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder done(String done) {
            this.done = done;
            return this;
        }

        public TaskDTO build() {
            TaskDTO taskDTO = new TaskDTO();
            taskDTO.id = this.id;
            taskDTO.title = this.title;
            taskDTO.done = this.done;
            return taskDTO;
        }
    }
}
