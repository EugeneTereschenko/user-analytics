package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profile_image")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "profile_id", length = 255)
    Long profileId;

    @Column(name = "details_id", length = 255)
    Long detailsId;

    public static class Builder {
        private Long profileId;
        private Long detailsId;

        public Builder profileId(Long profileId) {
            this.profileId = profileId;
            return this;
        }

        public Builder detailsId(Long detailsId) {
            this.detailsId = detailsId;
            return this;
        }

        public ProfileDetails build() {
            ProfileDetails profileDetails = new ProfileDetails();
            profileDetails.profileId = this.profileId;
            profileDetails.detailsId = this.detailsId;
            return profileDetails;
        }
    }
}
