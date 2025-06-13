package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "profile")
@NoArgsConstructor
@Data
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "firstname", length = 255)
    private String firstName;

    @Column(name = "lastname", length = 255)
    private String lastName;

    @Column(name = "phone", length = 255)
    private String phoneNumber;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "shippingAddress", length = 255)
    private String shippingAddress;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    @ManyToOne
    @JoinColumn(name = "details_id")
    private Details details;

    @ManyToMany
    @JoinTable(
            name = "profile_image",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    private Set<Image> image;

    private Profile(Builder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.phoneNumber = builder.phoneNumber;
        this.address = builder.address;
        this.shippingAddress = builder.shippingAddress;
        this.card = builder.card;
        this.image = builder.image != null ? builder.image : new HashSet<>();
    }

    public static class Builder {
        private Long id;
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private String address;
        private String shippingAddress;
        private Card card;
        private Set<Image> image;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder shippingAddress(String shippingAddress) {
            this.shippingAddress = shippingAddress;
            return this;
        }

        public Builder card(Card card) {
            this.card = card;
            return this;
        }

        public Builder image(Set<Image> image) {
            this.image = image;
            return this;
        }

        public Profile build() {
            return new Profile(this);
        }
    }
}