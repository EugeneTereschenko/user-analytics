package com.example.demo.controller;

import com.example.demo.dto.TaskDTO;
import com.example.demo.service.impl.TaskService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody TaskDTO taskDTO) {
        log.info("Task created successfully" + taskDTO);
        return ResponseEntity.ok(taskService.createTask(taskDTO));
    }


    @GetMapping
    public ResponseEntity<?> getTasks() {
        log.info("Fetching all tasks");
        return ResponseEntity.ok(taskService.getTaskByUser());
    }
}
