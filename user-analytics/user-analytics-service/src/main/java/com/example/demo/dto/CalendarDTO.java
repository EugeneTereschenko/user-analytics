package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CalendarDTO {
    private String title;
    private String date;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String title;
        private String date;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder date(String date) {
            this.date = date;
            return this;
        }

        public CalendarDTO build() {
            CalendarDTO calendarDTO = new CalendarDTO();
            calendarDTO.title = this.title;
            calendarDTO.date = this.date;
            return calendarDTO;
        }
    }
}
