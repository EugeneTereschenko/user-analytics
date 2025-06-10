package com.example.demo.dto;

public class UserSummaryDTO {
    public int totalUsers;
    public int activeUsers;
    public int newUsersToday;
    public double bounceRate;

    public UserSummaryDTO(int totalUsers, int activeUsers, int newUsersToday, double bounceRate) {
        this.totalUsers = totalUsers;
        this.activeUsers = activeUsers;
        this.newUsersToday = newUsersToday;
        this.bounceRate = bounceRate;
    }
}

