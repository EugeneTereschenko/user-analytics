package com.example.demo.controller;

import com.example.demo.dto.TaskDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/tasks")
public class TaskController {


    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody TaskDTO taskDTO) {
        log.info("Task created successfully" + taskDTO);
        return ResponseEntity.ok(new HashMap<>().put("message", "Task created successfully"));
    }


    @GetMapping
    public ResponseEntity<?> getTasks() {
        log.info("Fetching all tasks");
        List<TaskDTO> tasks = List.of(
                new TaskDTO("1", "Task 1", "Description for task 1"),
                new TaskDTO("2", "Task 2", "Description for task 2")
        );
        return ResponseEntity.ok(tasks);
    }
}
