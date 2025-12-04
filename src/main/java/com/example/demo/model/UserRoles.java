package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_roles")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserRoles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "user_id", length = 255)
    private Long userId;
    @Column(name = "role_id", length = 255)
    private Long roleId;

    public static class Builder {
        private Long userId;
        private Long roleId;

        public Builder userId(Long userId) {
            this.userId = userId;
            return this;
        }

        public Builder roleId(Long roleId) {
            this.roleId = roleId;
            return this;
        }

        public UserRoles build() {
            UserRoles userRoles = new UserRoles();
            userRoles.userId = this.userId;
            userRoles.roleId = this.roleId;
            return userRoles;
        }
    }
}
