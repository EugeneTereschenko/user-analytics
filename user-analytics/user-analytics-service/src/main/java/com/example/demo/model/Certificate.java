package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"certificate\"")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @Column(name = "certificate_name", length = 255)
    private String certificateName;
    @Column(name = "date_from", length = 255)
    private String dateFrom;
    @Column(name = "date_to", length = 255)
    private String dateTo;
    @Column(name = "country_city", length = 255)
    private String countryCity;
    @Column(name = "institution_name", length = 255)
    private String institutionName;
    @Column(name = "certificate_url", length = 255)
    private String certificateUrl;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String certificateName;
        private String dateFrom;
        private String dateTo;
        private String countryCity;
        private String institutionName;
        private String certificateUrl;

        public Builder certificateName(String certificateName) {
            this.certificateName = certificateName;
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

        public Builder institutionName(String institutionName) {
            this.institutionName = institutionName;
            return this;
        }

        public Builder certificateUrl(String certificateUrl) {
            this.certificateUrl = certificateUrl;
            return this;
        }

        public Certificate build() {
            Certificate certificate = new Certificate();
            certificate.certificateName = this.certificateName;
            certificate.dateFrom = this.dateFrom;
            certificate.dateTo = this.dateTo;
            certificate.countryCity = this.countryCity;
            certificate.institutionName = this.institutionName;
            certificate.certificateUrl = this.certificateUrl;
            return certificate;
        }
    }
}
