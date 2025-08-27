package com.example.demo.dto;

import lombok.ToString;

@ToString
public class UserSummaryDTO {
    public Long totalUsers;
    public Long activeUsers;
    public Long newUsersToday;
    public Double bounceRate;

    public UserSummaryDTO(Long totalUsers, Long activeUsers, Long newUsersToday, Double bounceRate) {
        this.totalUsers = totalUsers;
        this.activeUsers = activeUsers;
        this.newUsersToday = newUsersToday;
        this.bounceRate = bounceRate;
    }
}

