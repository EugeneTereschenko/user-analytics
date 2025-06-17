package com.example.demo.controller;

import com.example.demo.service.ProfileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ProfileController {

    private final ProfileService profileService;


    @PostMapping("profile/upload-image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String result = profileService.uploadImageToDatabase(file);
        if (!result.startsWith("Error")) {
            log.debug("Image upload successful: " + result);
            return ResponseEntity.ok(result);
        }
        log.error("Image upload failed: " + result);
        return ResponseEntity.status(500).body(result);
    }

    @GetMapping("profile/getImage")
    public ResponseEntity<byte[]> getImage() {
        byte[] imageDataResult = profileService.getImageForUser();
        if (imageDataResult != null) {
            log.debug("Image retrieval successful");
            return ResponseEntity.ok()
                    .header("Content-Type", "image/jpeg") // Adjust based on your image type
                    .body(imageDataResult);
        }
        log.error("Image retrieval failed");
        return ResponseEntity.status(500).body(null);
    }
}
