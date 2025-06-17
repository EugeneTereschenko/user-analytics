package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;


@Slf4j
@RequiredArgsConstructor
@Service
public class ProfileService {

    private final UserService userService;
    private final DetailsRepository detailsRepository;
    private final CardRepository cardRepository;
    private final UserProfileRepository userProfileRepository;
    private final ProfileRepository profileRepository;
    private final ProfileImageRepository profileImageRepository;
    private final ImageRepository imageRepository;

    public String uploadImageToDatabase(MultipartFile file) {
        try {
            Optional<User> user = userService.getAuthenticatedUser();
            List<Profile> profiles = profileRepository.findProfilesByUserId(user.get().getUserId());
            if (user.isPresent() && profiles.isEmpty()) {
                log.info("User {} has no profile, creating a new one.", user.get().getUsername());
                Profile profile = createProfile(user.get().getUserId());
                return String.valueOf(saveImage(file, profile.getId()).getId());
            }
            if (user.isPresent() && !profiles.isEmpty()) {
                log.info("User {} has existing profiles, saving image to the first profile.", user.get().getUsername());
                return String.valueOf(saveImage(file, profiles.get(0).getId()).getId());
            }
        } catch (Exception e) {
            log.error("Error uploading image: {}", e.getMessage());
            return "Error uploading image: " + e.getMessage();
        }
        return "";
    }

    @Transactional
    public byte[] getImageForUser() {
        try {
            Optional<User> user = userService.getAuthenticatedUser();
            if (user.isEmpty()) {
                log.warn("No authenticated user found.");
                return null;
            }

            List<Image> images = profileRepository.findImagesByUserId(user.get().getUserId());
            if (images.isEmpty()) {
                log.warn("No images found for user: {}", user.get().getUsername());
                return null;
            }

            Image lastImage = images.stream().reduce((first, second) -> second).orElse(null);
            if (lastImage == null || lastImage.getData() == null) {
                log.warn("No valid image data found for user: {}", user.get().getUsername());
                return null;
            }

            return lastImage.getData();
        } catch (Exception e) {
            log.error("Error retrieving image for user: {}", e.getMessage(), e);
            return null;
        }
    }

    @Transactional
    private Image saveImage(MultipartFile file, Long profileId) throws IOException {

        Image existingImage = imageRepository.saveAndFlush(new Image.Builder()
                .name(file.getOriginalFilename())
                .data(file.getBytes())
                .build());

        if (existingImage == null) {
            log.error("Failed to save image for profile ID: {}", profileId);
            throw new RuntimeException("Failed to save image for profile ID: " + profileId);

        }
        profileImageRepository.saveAndFlush(new ProfileImage.Builder()
                .profileId(profileId)
                .imageId(existingImage.getId())
                .build());
        return existingImage;
    }

    @Transactional
    public Profile createProfile(Long userId) {
        Profile existsProfile = profileRepository.saveAndFlush(new Profile());
        UserProfile existsUserProfile = userProfileRepository.saveAndFlush(new UserProfile.Builder()
                .userId(userId)
                .profileId(existsProfile.getId())
                .build());
        if (existsUserProfile != null) {
            return existsProfile;
        }
        log.error("Failed to create profile for user ID: {}", userId);
        throw new RuntimeException("Failed to create profile for user ID: " + userId);
    }

}
