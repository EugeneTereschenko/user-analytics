package com.example.demo.service;

import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.model.UserRoles;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.UserRolesRepository;
import com.example.demo.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRolesRepository userRolesRepository;
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    public UserResponseDTO loginUser(UserRequestDTO userDTO) {
        log.debug("Attempting to login user: {}", userDTO.toString());
        try {
            Optional<User> userOptional = userRepository.findByEmail(userDTO.getEmail());
            if (userOptional.isEmpty()) {
                return createUserResponseDTO("", "Login failed: User not found", "false");
            }

            User user = userOptional.get();
            log.debug("User found: {}", user.getUsername());
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());
            if (userDetails == null) {
                return createUserResponseDTO("", "Login failed: User details not found", "false");
            }
            log.debug("User details loaded for: {}", userDetails.getUsername());
            String token = jwtUtil.generateToken(userDetails.getUsername());
            return createUserResponseDTO(token, "Login successful", "true");
        } catch (Exception e) {
            log.error("Failed to login: {}", e.getMessage());
            return createUserResponseDTO("", "Login failed: " + e.getMessage(), "false");
        }
    }

    public UserResponseDTO createUser(UserRequestDTO userDTO) {
        try {
            if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
                return createUserResponseDTO("", "Signup failed: Email already exists", "false");
            }
            log.debug("Creating user with email: {}", userDTO.getEmail());
            Collection<Role> roles = roleRepository.findByNameIn(userDTO.getRoles());
            if (roles.isEmpty()) {
                return createUserResponseDTO("", "Signup failed: Invalid roles", "false");
            }
            log.debug("Roles found for user: {}", roles);
            User user = new User.Builder()
                    .username(userDTO.getUsername())
                    .email(userDTO.getEmail())
                    .password(passwordEncoder.encode(userDTO.getPassword()))
                    .salt(userDTO.getPassword())// Assuming salt is the same as password for simplicity
                    .isActive(true)
                    .roles(roles)
                    .isTwoFactorEnabled(false)
                    .build();

            log.debug("User created: {}", user.getUsername());
            userRepository.save(user);
            log.debug("User saved to repository: {}", user.getUsername());
            UserRoles userRoles = new UserRoles.Builder()
                    .userId(user.getUserId())
                    .roleId(roles.stream().findFirst().get().getId()) // Assuming one role for simplicity
                    .build();

            log.debug("UserRoles created for user: {}", userRoles.getRoleId(), "with userId: {}", userRoles.getUserId());
            userRolesRepository.save(userRoles);
            return createUserResponseDTO("", "Signup successful", "true");
        } catch (Exception e) {
            log.error("Failed to create user: {}", e.getMessage());
            return createUserResponseDTO("", "Signup failed: " + e.getMessage(), "false");
        }
    }

    public Optional<User> getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new UsernameNotFoundException("User not authenticated");
        }
        String username = authentication.getName();
        return userRepository.findByUsername(username);
    }

    private UserResponseDTO createUserResponseDTO(String token, String message, String success) {
        return new UserResponseDTO.Builder()
                .token(token)
                .message(message)
                .success(success)
                .build();
    }
}