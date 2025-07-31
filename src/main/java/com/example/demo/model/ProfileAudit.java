package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profile_audit")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "profile_id")
    private Long profileId;
    @Column(name = "audit_id")
    private Long auditId;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long profileId;
        private Long auditId;

        public Builder profileId(Long profileId) {
            this.profileId = profileId;
            return this;
        }

        public Builder auditId(Long auditId) {
            this.auditId = auditId;
            return this;
        }

        public ProfileAudit build() {
            ProfileAudit profileAudit = new ProfileAudit();
            profileAudit.profileId = this.profileId;
            profileAudit.auditId = this.auditId;
            return profileAudit;
        }
    }
}
