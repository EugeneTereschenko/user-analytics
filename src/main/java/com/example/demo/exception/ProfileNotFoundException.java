package com.example.demo.exception;

public class ProfileNotFoundException extends RuntimeException {
    public ProfileNotFoundException(String username) {
        super("No profile found for user: " + username);
    }

    public ProfileNotFoundException(Long userId) {
        super("No profile found for user ID: " + userId);
    }
}
