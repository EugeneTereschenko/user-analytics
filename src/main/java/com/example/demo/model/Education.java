package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"education\"")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(name = "university_name", length = 255)
    private String universityName;
    @Column(name = "date_from", length = 255)
    private String dateFrom;
    @Column(name = "date_to", length = 255)
    private String dateTo;
    @Column(name = "country_city", length = 255)
    private String countryCity;
    @Column(name = "degree", length = 255)
    private String degree;

    public static Builder builder() {
        return new Builder();
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

        public Education build() {
            Education education = new Education();
            education.universityName = this.universityName;
            education.dateFrom = this.dateFrom;
            education.dateTo = this.dateTo;
            education.countryCity = this.countryCity;
            education.degree = this.degree;
            return education;
        }
    }
}
