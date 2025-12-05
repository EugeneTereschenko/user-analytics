package com.example.demo.dto;

import lombok.Data;

@Data
public class EducationDTO {
    public String universityName;
    public String dateFrom;
    public String dateTo;
    public String countryCity;
    public String degree;

    public static EducationDTO.Builder builder() {
        return new EducationDTO.Builder();
    }

    public static class Builder {
        private String universityName;
        private String dateFrom;
        private String dateTo;
        private String countryCity;
        private String degree;

        public Builder universityName(String universityName) {
            this.universityName = universityName;
            return this;
        }

        public Builder dateFrom(String dateFrom) {
            this.dateFrom = dateFrom;
            return this;
        }

        public Builder dateTo(String dateTo) {
            this.dateTo = dateTo;
            return this;
        }

        public Builder countryCity(String countryCity) {
            this.countryCity = countryCity;
            return this;
        }

        public Builder degree(String degree) {
            this.degree = degree;
            return this;
        }

        public EducationDTO build() {
            EducationDTO educationDTO = new EducationDTO();
            educationDTO.universityName = this.universityName;
            educationDTO.dateFrom = this.dateFrom;
            educationDTO.dateTo = this.dateTo;
            educationDTO.countryCity = this.countryCity;
            educationDTO.degree = this.degree;
            return educationDTO;
        }
    }
}
