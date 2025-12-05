package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"experience\"")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name = "role_name", length = 255)
    private String roleName;

    @Column(name = "date_from", length = 255)
    private String dateFrom;

    @Column(name = "date_to", length = 255)
    private String dateTo;

    @Column(name = "company_name", length = 255)
    private String companyName;

    @Column(name = "country_city", length = 255)
    private String countryCity;

    @Column(name = "service", length = 255)
    private String service;

    public static Builder builder() {
        return new Builder();
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

        public Experience build() {
            Experience experience = new Experience();
            experience.roleName = this.roleName;
            experience.dateFrom = this.dateFrom;
            experience.dateTo = this.dateTo;
            experience.companyName = this.companyName;
            experience.countryCity = this.countryCity;
            experience.service = this.service;
            return experience;
        }
    }
}
