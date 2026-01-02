package com.example.demo.service;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TaskDTO;
import com.example.demo.model.*;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.repository.ProfileTaskRepository;
import com.example.demo.repository.TaskRepository;
import com.example.demo.service.impl.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProfileTaskRepository profileTaskRepository;
    private final UserService userService;
    private final ProfileRepository profileRepository;


    public ResponseDTO createTask(TaskDTO taskDTO) {
        if (taskRepository.findByTitle(taskDTO.getTitle()).isPresent()) {
            return ResponseDTO.builder()
                    .status("error")
                    .message("Task with this name already exists")
                    .build();
        }

        Task task = Task.builder()
                .title(taskDTO.getTitle())
                .done(taskDTO.getDone())
                .build();

        Task savedTask = taskRepository.save(task);
        saveProfileTask(savedTask.getId());

        return ResponseDTO.builder()
                .status("success")
                .message("Task created successfully")
                .build();
    }

    public List<TaskDTO> getTaskByUser() {
        User user = userService.getAuthenticatedUser()
                .orElseThrow(() -> new RuntimeException("User not authenticated"));

        return taskRepository.findTasksByUserId(user.getId())
                .stream()
                .map(task -> TaskDTO.builder()
                        .id(String.valueOf(task.getId()))
                        .title(task.getTitle())
                        .done(task.getDone())
                        .build())
                .toList();
    }

    private Boolean saveProfileTask(Long taskId) {
        User user = userService.getAuthenticatedUser()
                .orElseThrow(() -> new RuntimeException("User not authenticated"));

        Profile profile = profileRepository.findProfilesByUserId(user.getId())
                .stream()
                .reduce((first, second) -> second)
                .orElseGet(() -> profileRepository.saveAndFlush(new Profile()));

        ProfileTask profileTask = ProfileTask.builder()
                .profileId(profile.getId())
                .taskId(taskId)
                .build();

        profileTaskRepository.saveAndFlush(profileTask);
        return true; // Assuming the save operation is successful
    }
}
