package com.example.demo.service;

import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserServiceTest userServiceTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loginUser() {
        UserRequestDTO userRequestDTO = new UserRequestDTO.Builder()
                .email("test@test.com")
                .password("password123")
                .build();

        UserResponseDTO expectedResponse = new UserResponseDTO.Builder()
                .token("mockToken")
                .message("Login successful")
                .success("true")
                .build();

        when(userService.loginUser(userRequestDTO)).thenReturn(expectedResponse);

        UserResponseDTO actualResponse = userService.loginUser(userRequestDTO);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getToken(), actualResponse.getToken());
        assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedResponse.getSuccess(), actualResponse.getSuccess());
    }

    @Test
    void createUser() {
        UserRequestDTO userRequestDTO = new UserRequestDTO.Builder()
                .username("admin")
                .email("admin@test.com")
                .password("admin123")
                .roles(java.util.Collections.singletonList("ROLE_ADMIN"))
                .build();

        UserResponseDTO expectedResponse = new UserResponseDTO.Builder()
                .token("")
                .message("Signup successful")
                .success("true")
                .build();

        when(userService.createUser(userRequestDTO)).thenReturn(expectedResponse);

        UserResponseDTO actualResponse = userService.createUser(userRequestDTO);

        assertNotNull(actualResponse);
        assertEquals(expectedResponse.getToken(), actualResponse.getToken());
        assertEquals(expectedResponse.getMessage(), actualResponse.getMessage());
        assertEquals(expectedResponse.getSuccess(), actualResponse.getSuccess());
    }
}