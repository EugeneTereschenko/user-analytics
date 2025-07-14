package com.example.demo.controller;

import com.example.demo.dto.UserDetailDTO;
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
@RequestMapping("/api/")
public class UserDetailController {


    @GetMapping("user/{id}")
    public ResponseEntity<?> getUserDetailsById(@PathVariable("id") String userId) {
        log.info("Fetching user details for id: {}", userId);
        UserDetailDTO userDetailDTO = new UserDetailDTO.Builder()
                .name("John Doe")
                .role("Admin")
                .email("test@com")
                .build();
        return ResponseEntity.ok(userDetailDTO);
    }


    @GetMapping("users")
    public ResponseEntity<?> getAllUserDetails() {
        log.info("Fetching user details");
        List<UserDetailDTO> userDetails = new ArrayList<>();
        UserDetailDTO userDetailDTO = new UserDetailDTO.Builder()
                .name("John Doe")
                .role("Admin")
                .email("test@com")
                .build();
        userDetails.add(userDetailDTO);
        userDetailDTO = new UserDetailDTO.Builder()
                .name("Jane Smith")
                .role("User")
                .email("jane@com")
                .build();
        userDetails.add(userDetailDTO);

        return ResponseEntity.ok(userDetails);
    }
}
