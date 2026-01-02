package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReminderDTO {
    private String id;
    private String title;
    private String date;
    private String time;
    private String notified;


    public static class Builder {
        private String id;
        private String title;
        private String date;
        private String time;
        private String notified;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder date(String date) {
            this.date = date;
            return this;
        }

        public Builder time(String time) {
            this.time = time;
            return this;
        }

        public Builder notified(String notified) {
            this.notified = notified;
            return this;
        }

        public ReminderDTO build() {
            ReminderDTO reminderDTO = new ReminderDTO();
            reminderDTO.id = this.id;
            reminderDTO.title = this.title;
            reminderDTO.date = this.date;
            reminderDTO.time = this.time;
            reminderDTO.notified = this.notified;
            return reminderDTO;
        }


    }

}
