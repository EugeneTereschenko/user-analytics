package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profile_skills")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileSkills {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_id", length = 255)
    private Long profileId;

    @Column(name = "skill_id", length = 255)
    private Long skillsId;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long profileId;
        private Long skillsId;

        public Builder profileId(Long profileId) {
            this.profileId = profileId;
            return this;
        }

        public Builder skillsId(Long skillsId) {
            this.skillsId = skillsId;
            return this;
        }

        public ProfileSkills build() {
            ProfileSkills profileSkills = new ProfileSkills();
            profileSkills.profileId = this.profileId;
            profileSkills.skillsId = this.skillsId;
            return profileSkills;
        }
    }
}
