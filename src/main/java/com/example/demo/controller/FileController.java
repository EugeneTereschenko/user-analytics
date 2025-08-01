package com.example.demo.controller;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.model.FileEntity;
import com.example.demo.service.impl.FileService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/files")
public class FileController {

    private FileService fileService;

    @GetMapping
    public ResponseEntity<List<String>> fetchFiles() {
        return ResponseEntity.ok(fileService.getAllFileNamesByUser());
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<?> fetchFileByName(@PathVariable("fileName") String fileName ) {
        try {
            FileEntity fileEntity = fileService.getFileByFileName(fileName);
            log.info("File fetched: {}", fileEntity.getFileName());
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"" + fileEntity.getFileName() + "\"")
                    .body(fileEntity.getFileData());
        } catch (IOException e) {
            log.error("Error fetching file: {}", e.getMessage());
            return ResponseEntity.status(500).body(new ResponseDTO.Builder()
                    .message("File not found")
                    .status("error")
                    .build());
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            FileEntity savedFile = fileService.saveFile(file);
            log.info("File saved: {}", savedFile.getFileName());
            return ResponseEntity.ok(new ResponseDTO.Builder()
                    .message("File uploaded successfully: " + savedFile.getFileName())
                    .status("success")
                    .build());
        } catch (IOException e) {
            log.error("Error saving file: {}", e.getMessage());
            return ResponseEntity.status(500).body(new ResponseDTO.Builder()
                    .message("File upload failed")
                    .status("error")
                    .build());
        }
    }

}
