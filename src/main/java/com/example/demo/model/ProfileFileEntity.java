package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profile_file_entity")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_id", length = 255)
    private Long profileId;

    @Column(name = "file_id", length = 255)
    private Long fileId;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long profileId;
        private Long fileId;

        public Builder profileId(Long profileId) {
            this.profileId = profileId;
            return this;
        }

        public Builder fileId(Long fileId) {
            this.fileId = fileId;
            return this;
        }

        public ProfileFileEntity build() {
            ProfileFileEntity profileFileEntity = new ProfileFileEntity();
            profileFileEntity.profileId = this.profileId;
            profileFileEntity.fileId = this.fileId;
            return profileFileEntity;
        }
    }
}
