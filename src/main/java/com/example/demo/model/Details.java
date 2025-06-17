package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"details\"")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Details {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "notification", length = 255)
    private Boolean notification;

    @Column(name = "staff", length = 255)
    private String staff;

    @Column(name = "bio", length = 255)
    private String bio;

    @Column(name = "message", length = 255)
    private String message;
}
