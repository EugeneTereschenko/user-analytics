package com.example.demo.mapper;

import com.example.demo.dto.ProfileDTO;
import com.example.demo.model.Profile;
import com.example.demo.model.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class ProfileMapperTest {

    @Mock
    private ProfileMapper profileMapper = new ProfileMapper();

    @Test
    public void testProfileMapping() {
        // Arrange
        User user = new User();
        user.setEmail("test@example.com");

        Profile profile = new Profile.Builder()
                .firstName("John")
                .lastName("Doe")
                .linkedin("https://linkedin.com/in/johndoe")
                .skype("john.doe.skype")
                .github("https://github.com/johndoe")
                .build();

        // Act
        ProfileDTO dto = profileMapper.toProfileDTO(profile, user);

        // Assert
        assertEquals("https://linkedin.com/in/johndoe", dto.getLinkedin());
        assertEquals("john.doe.skype", dto.getSkype());
        assertEquals("https://github.com/johndoe", dto.getGithub());
    }

}