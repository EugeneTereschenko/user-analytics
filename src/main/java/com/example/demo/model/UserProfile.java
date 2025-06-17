package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users_profile")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", length = 255)
    private Long userId;
    @Column(name = "profile_id", length = 255)
    private Long profileId;

    public static class Builder {
        private Long userId;
        private Long profileId;

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder profileId(Long profileId) {
            this.profileId = profileId;
            return this;
        }

        public UserProfile build() {
            UserProfile userProfile = new UserProfile();
            userProfile.userId = this.userId;
            userProfile.profileId = this.profileId;
            return userProfile;
        }
    }
}
