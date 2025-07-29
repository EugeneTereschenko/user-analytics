package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profile_support")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileSupport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_id", length = 255)
    private Long profileId;

    @Column(name = "support_id", length = 255)
    private Long supportId;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long profileId;
        private Long supportId;

        public Builder profileId(Long profileId) {
            this.profileId = profileId;
            return this;
        }

        public Builder supportId(Long supportId) {
            this.supportId = supportId;
            return this;
        }

        public ProfileSupport build() {
            ProfileSupport profileSupport = new ProfileSupport();
            profileSupport.profileId = this.profileId;
            profileSupport.supportId = this.supportId;
            return profileSupport;
        }
    }

}
