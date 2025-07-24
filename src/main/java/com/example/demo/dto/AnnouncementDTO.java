package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AnnouncementDTO {
    private String title;
    private String body;
    private String date;

    public static class Builder {
        private String title;
        private String body;
        private String date;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder date(String date) {
            this.date = date;
            return this;
        }

        public AnnouncementDTO build() {
            AnnouncementDTO announcementDTO = new AnnouncementDTO();
            announcementDTO.title = this.title;
            announcementDTO.body = this.body;
            announcementDTO.date = this.date;
            return announcementDTO;
        }
    }


}
