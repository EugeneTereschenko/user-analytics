package com.example.demo.exception;

public class UserNotAuthenticatedException extends RuntimeException {
    public UserNotAuthenticatedException() {
        super("No authenticated user found");
    }
}
