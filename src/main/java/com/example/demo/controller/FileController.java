package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/files")
public class FileController {

    @GetMapping
    public ResponseEntity<List<String>> fetchFiles() {
        // TODO: Replace with actual file fetching logic
        return ResponseEntity.ok(List.of("file1.txt", "file2.txt"));
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        // TODO: Implement file saving logic
        log.info("Received file: {}", file.getOriginalFilename());
        return ResponseEntity.ok(new ResponseDTO.Builder()
                .message("File uploaded successfully: " + file.getOriginalFilename())
                .status("success")
                .build());
    }

}
