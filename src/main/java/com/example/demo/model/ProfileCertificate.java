package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profile_certificate")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_id", length = 255)
    private Long profileId;

    @Column(name = "certificate_id", length = 255)
    private Long certificateId;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long profileId;
        private Long certificateId;

        public Builder profileId(Long profileId) {
            this.profileId = profileId;
            return this;
        }

        public Builder certificateId(Long certificateId) {
            this.certificateId = certificateId;
            return this;
        }

        public ProfileCertificate build() {
            ProfileCertificate profileCertificate = new ProfileCertificate();
            profileCertificate.profileId = this.profileId;
            profileCertificate.certificateId = this.certificateId;
            return profileCertificate;
        }
    }
}
