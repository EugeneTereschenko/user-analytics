package com.example.demo.dto;

import lombok.Data;

@Data
public class ExperienceDTO {
    public String roleName;
    public String dateFrom;
    public String dateTo;
    public String companyName;
    public String countryCity;
    public String service;

    public static ExperienceDTO.Builder builder() {
        return new ExperienceDTO.Builder();
    }

    public static class Builder {
        private String roleName;
        private String dateFrom;
        private String dateTo;
        private String companyName;
        private String countryCity;
        private String service;

        public Builder roleName(String roleName) {
            this.roleName = roleName;
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

        public Builder companyName(String companyName) {
            this.companyName = companyName;
            return this;
        }

        public Builder countryCity(String countryCity) {
            this.countryCity = countryCity;
            return this;
        }

        public Builder service(String service) {
            this.service = service;
            return this;
        }

        public ExperienceDTO build() {
            ExperienceDTO experienceDTO = new ExperienceDTO();
            experienceDTO.roleName = this.roleName;
            experienceDTO.dateFrom = this.dateFrom;
            experienceDTO.dateTo = this.dateTo;
            experienceDTO.companyName = this.companyName;
            experienceDTO.countryCity = this.countryCity;
            experienceDTO.service = this.service;
            return experienceDTO;
        }
    }
}
