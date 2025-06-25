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
public class ProfileImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "profile_id", length = 255)
    Long profileId;

    @Column(name = "image_id", length = 255)
    Long imageId;

    public static class Builder {
        private Long profileId;
        private Long imageId;

        public Builder profileId(Long profileId) {
            this.profileId = profileId;
            return this;
        }

        public Builder imageId(Long imageId) {
            this.imageId = imageId;
            return this;
        }

        public ProfileImage build() {
            ProfileImage profileImage = new ProfileImage();
            profileImage.profileId = this.profileId;
            profileImage.imageId = this.imageId;
            return profileImage;
        }
    }
}
