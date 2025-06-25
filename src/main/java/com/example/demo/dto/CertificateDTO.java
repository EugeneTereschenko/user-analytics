package com.example.demo.dto;

import lombok.Data;

@Data
public class CertificateDTO {
    public String certificateName;
    public String dateFrom;
    public String dateTo;
    public String countryCity;
    public String institutionName;
    public String certificateUrl;

    public static CertificateDTO .Builder builder() {
        return new CertificateDTO .Builder();
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

        public CertificateDTO build() {
            CertificateDTO dto = new CertificateDTO();
            dto.certificateName = this.certificateName;
            dto.dateFrom = this.dateFrom;
            dto.dateTo = this.dateTo;
            dto.countryCity = this.countryCity;
            dto.institutionName = this.institutionName;
            dto.certificateUrl = this.certificateUrl;
            return dto;
        }
    }
}
