package com.example.demo.controller;

import com.example.demo.dto.UserDetailDTO;
import com.example.demo.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/")
public class UserDetailController {

    private final UserService userService;

    @GetMapping("user/{id}")
    public ResponseEntity<?> getUserDetailsById(@PathVariable("id") String userId) {
        log.info("Fetching user details for id: {}", userId);
        return ResponseEntity.ok(userService.getUserDetailsById(userId));
    }


    @GetMapping("users")
    public ResponseEntity<?> getAllUserDetails() {
        log.info("Fetching user details");
        return ResponseEntity.ok(userService.getAllUserDetails());
    }
}
