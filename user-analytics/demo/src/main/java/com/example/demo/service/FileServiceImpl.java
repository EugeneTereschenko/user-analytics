package com.example.demo.service;


import com.example.demo.model.*;
import com.example.demo.repository.FileRepository;
import com.example.demo.repository.ProfileFileEntityRepository;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.service.impl.FileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final ProfileFileEntityRepository profileFileEntityRepository;
    private final UserService userService;
    private final ProfileRepository profileRepository;

    @Transactional
    public List<FileEntity> getAllFilesByUser() {
        return fileRepository.findFileByUserId(userService.getAuthenticatedUser().get().getId());
    }

    @Transactional
    public List<String> getAllFileNamesByUser() {
        return fileRepository.findFileNamesByUserId(userService.getAuthenticatedUser().get().getId())
                .stream()
                .map(FileEntity::getFileName).toList();
    }

    @Transactional
    public FileEntity getFileByFileName(String fileName) throws IOException {
        return fileRepository.findByFileNameAndUserId(userService.getAuthenticatedUser().get().getId(), fileName);

    }

    @Transactional
    @Override
    public void deleteFileByFileName(String fileName) throws IOException {
        FileEntity fileEntity = fileRepository.findByFileNameAndUserId(userService.getAuthenticatedUser().get().getId(), fileName);
        if (fileEntity != null) {
            fileRepository.delete(fileEntity);
            profileFileEntityRepository.deleteByFileId(fileEntity.getId());
        } else {
            throw new IOException("File not found");
        }
    }

    @Transactional
    @Override
    public FileEntity saveFile(MultipartFile file) throws IOException {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setFileName(file.getOriginalFilename());
        fileEntity.setFileData(file.getBytes());
        fileEntity = fileRepository.save(fileEntity);
        saveProfileFileEntity(fileEntity.getId());
        return fileEntity;
    }

    private Boolean saveProfileFileEntity(Long fileEntityId) {
        User user = userService.getAuthenticatedUser()
                .orElseThrow(() -> new RuntimeException("User not authenticated"));

        Profile profile = profileRepository.findProfilesByUserId(user.getId())
                .stream()
                .reduce((first, second) -> second)
                .orElseGet(() -> profileRepository.saveAndFlush(new Profile()));

        ProfileFileEntity profileFileEntity = ProfileFileEntity.builder()
                .profileId(profile.getId())
                .fileId(fileEntityId)
                .build();

        profileFileEntityRepository.saveAndFlush(profileFileEntity);
        return true; // Assuming the save operation is successful
    }
}
