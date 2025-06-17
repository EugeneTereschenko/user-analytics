package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Table(name = "\"users\"")
@Entity
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "email", nullable = false, length = 255)
    private String email;
    @Column(name = "username", nullable = false, length = 255)
    private String username;
    @Column(name = "password", nullable = false, length = 255)
    private String password;
    @Column(name = "password_generate", nullable = false, length = 255)
    private String salt;
    @Column(name = "secret_key")
    private String SecretKey;

    @Column(name = "is_active", length = 255)
    private boolean isActive;
    @Column(name = "is_two_factor", length = 255)
    private boolean isTwoFactorEnabled;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Role> roles = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public static class Builder {
        private Long userId;
        private String email;
        private String username;
        private String password;
        private String salt;
        private String SecretKey;
        private boolean isActive;
        private boolean isTwoFactorEnabled;
        private Collection<Role> roles = new ArrayList<>();

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder salt(String salt) {
            this.salt = salt;
            return this;
        }

        public Builder secretKey(String secretKey) {
            this.SecretKey = secretKey;
            return this;
        }

        public Builder isActive(boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public Builder isTwoFactorEnabled(boolean isTwoFactorEnabled) {
            this.isTwoFactorEnabled = isTwoFactorEnabled;
            return this;
        }

        public Builder roles(Collection<Role> roles) {
            this.roles = roles;
            return this;
        }

        public User build() {
            User user = new User();
            user.userId = this.userId;
            user.email = this.email;
            user.username = this.username;
            user.password = this.password;
            user.salt = this.salt;
            user.SecretKey = this.SecretKey;
            user.isActive = this.isActive;
            user.isTwoFactorEnabled = this.isTwoFactorEnabled;
            user.roles = this.roles;
            return user;
        }
    }

}
