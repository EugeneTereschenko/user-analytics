package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profile_card")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_id", length = 255)
    private Long profileId;
    @Column(name = "card_id", length = 255)
    private Long cardId;

    public static class Builder {
        private Long profileId;
        private Long cardId;

        public Builder profileId(Long profileId) {
            this.profileId = profileId;
            return this;
        }

        public Builder cardId(Long cardId) {
            this.cardId = cardId;
            return this;
        }

        public ProfileCard build() {
            ProfileCard profileCard = new ProfileCard();
            profileCard.profileId = this.profileId;
            profileCard.cardId = this.cardId;
            return profileCard;
        }
    }
}
