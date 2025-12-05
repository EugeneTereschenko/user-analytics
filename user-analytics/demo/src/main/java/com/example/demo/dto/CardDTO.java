package com.example.demo.dto;

import lombok.Data;

@Data
public class CardDTO {
    public String cardNumber;
    public String expirationDate;
    public String cvv;
    public String cardName;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String cardNumber;
        private String expirationDate;
        private String cvv;
        private String cardName;

        public Builder cardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
            return this;
        }

        public Builder expirationDate(String expirationDate) {
            this.expirationDate = expirationDate;
            return this;
        }

        public Builder cvv(String cvv) {
            this.cvv = cvv;
            return this;
        }

        public Builder cardName(String cardName) {
            this.cardName = cardName;
            return this;
        }

        public CardDTO build() {
            CardDTO cardDTO = new CardDTO();
            cardDTO.cardNumber = this.cardNumber;
            cardDTO.expirationDate = this.expirationDate;
            cardDTO.cvv = this.cvv;
            cardDTO.cardName = this.cardName;
            return cardDTO;
        }
    }
}

