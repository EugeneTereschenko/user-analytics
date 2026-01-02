package com.example.demo.service;

import com.example.demo.dto.ProfileDTO;
import com.example.demo.dto.ResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@Transactional
public class ProfileServiceIntegrationTest {

    @Autowired
    private ProfileService profileService;

/*    @Test
    public void testUpdateProfileWithSocialFields() {
        // Create test data
        ProfileDTO dto = new ProfileDTO.Builder()
                .firstName("Jane")
                .lastName("Smith")
                .linkedin("https://linkedin.com/in/janesmith")
                .build();

        // Update profile
        ResponseDTO response = profileService.updateProfile(dto);

        // Verify
        assertEquals("200", response.getStatus());

        // Retrieve and check
        ProfileDTO retrieved = profileService.getProfile();
        assertEquals("https://linkedin.com/in/janesmith", retrieved.getLinkedin());
    }*/
}
