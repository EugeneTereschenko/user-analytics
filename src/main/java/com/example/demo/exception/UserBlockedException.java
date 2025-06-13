package com.example.demo.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserBlockedException extends UsernameNotFoundException {
    public UserBlockedException(String msg) {
        super(msg);
    }
}
