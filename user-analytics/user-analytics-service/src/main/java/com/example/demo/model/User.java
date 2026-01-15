package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Table(name = "users")
@Entity
@Data
public class User implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    @Column(name = "password_generate", nullable = false, length = 255)
    private String salt;
    @Column(name = "secret_key")
    private String SecretKey;
    @Column(name = "login_count")
    private int loginCount;
    @Column(name = "signup_date", columnDefinition = "DATE")
    private LocalDate signupDate;
    @Column(name = "last_login", columnDefinition = "TIMESTAMP")
    private LocalDateTime lastLogin;
    @Column(name = "is_active", length = 255)
    private boolean isActive;
    @Column(name = "is_two_factor", length = 255)
    private boolean isTwoFactorEnabled;

    // Location and device info (you already have these)
    private String location;
    private String deviceType;
    @Column(name = "activity_score")
    private double activityScore;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Activity tracking fields
    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "activity_count", nullable = false)
    private Integer activityCount = 0;

    // User status
    @Column(name = "enabled")
    private Boolean enabled = true;

    @Column(name = "account_non_expired")
    private Boolean accountNonExpired = true;

    @Column(name = "account_non_locked")
    private Boolean accountNonLocked = true;

    @Column(name = "credentials_non_expired")
    private Boolean credentialsNonExpired = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> roles;



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
        private int loginCount;
        private LocalDate signupDate;
        private LocalDateTime lastLogin;
        private int activityCount;
        private boolean isActive;
        private boolean isTwoFactorEnabled;
        private String location;
        private String deviceType;
        private double activityScore;
        private LocalDateTime createdAt;
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

        public Builder loginCount(int loginCount) {
            this.loginCount = loginCount;
            return this;
        }

        public Builder signupDate(LocalDate signupDate) {
            this.signupDate = signupDate;
            return this;
        }

        public Builder lastLogin(LocalDateTime lastLogin) {
            this.lastLogin = lastLogin;
            return this;
        }

        public Builder activityCount(int activityCount) {
            this.activityCount = activityCount;
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

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder deviceType(String deviceType) {
            this.deviceType = deviceType;
            return this;
        }

        public Builder activityScore(double activityScore) {
            this.activityScore = activityScore;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder roles(Collection<Role> roles) {
            this.roles = roles;
            return this;
        }

        public User build() {
            User user = new User();
            user.id = this.userId;
            user.email = this.email;
            user.username = this.username;
            user.password = this.password;
            user.salt = this.salt;
            user.SecretKey = this.SecretKey;
            user.loginCount = this.loginCount;
            user.signupDate = this.signupDate;
            user.lastLogin = this.lastLogin;
            user.activityCount = this.activityCount;
            user.isActive = this.isActive;
            user.isTwoFactorEnabled = this.isTwoFactorEnabled;
            user.location = this.location;
            user.deviceType = this.deviceType;
            user.activityScore = this.activityScore;
            user.createdAt = this.createdAt;
            user.roles = this.roles;
            return user;
        }
    }

}
