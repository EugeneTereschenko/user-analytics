package com.example.demo.service.impl;

import com.example.demo.model.FileEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    List<FileEntity> getAllFilesByUser();
    List<String> getAllFileNamesByUser();
    FileEntity saveFile(MultipartFile file) throws IOException;
}
