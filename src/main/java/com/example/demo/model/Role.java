package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import static jakarta.persistence.GenerationType.AUTO;

@Entity
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    private String name;

    public Role() {
    }
    public Role(String name) {
        this.name = name;
    }
    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
