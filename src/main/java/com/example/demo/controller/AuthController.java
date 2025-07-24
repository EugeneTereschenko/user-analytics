package com.example.demo.controller;

import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final UserService userService;

    @PostMapping("/auth/signin")
    ResponseEntity<?> signIn(@Valid @RequestBody  UserRequestDTO userRequestDTO) {
        UserResponseDTO userResponseDTO = userService.loginUser(userRequestDTO);
        if(userResponseDTO.getSuccess() == null || userResponseDTO.getSuccess().equals("false")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userResponseDTO);
        }
        return ResponseEntity.ok(userResponseDTO);
    }

    @PostMapping("/auth/signup")
    ResponseEntity<?> signUp(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        log.info("User signed up");
        UserResponseDTO userResponseDTO = userService.createUser(userRequestDTO);
        if(userResponseDTO.getSuccess() == null || userResponseDTO.getSuccess().equals("false")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userResponseDTO);
        }
        return ResponseEntity.ok(userResponseDTO);
    }

    @GetMapping("/roles")
    public ResponseEntity<?> getRoles() {
        log.info("Fetching roles");
        return ResponseEntity.ok(userService.getRoles());
    }
}
