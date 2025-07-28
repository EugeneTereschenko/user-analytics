package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AnnouncementDTO {
    private Long id;
    private String title;
    private String body;
    private String date;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String title;
        private String body;
        private String date;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

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
            announcementDTO.id = this.id;
            announcementDTO.title = this.title;
            announcementDTO.body = this.body;
            announcementDTO.date = this.date;
            return announcementDTO;
        }
    }


}
