package com.example.demo.controller;

import com.example.demo.dto.UserRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api")
public class AuthController {


    @PostMapping("/auth/signin")
    ResponseEntity<?> signIn(@Valid @RequestBody  UserRequestDTO userRequest) {
        log.info("User signed in");
        // Logic for signing in the user
        return ResponseEntity.ok().build();
    }
}
