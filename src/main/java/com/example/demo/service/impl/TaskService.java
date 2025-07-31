package com.example.demo.service.impl;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.TaskDTO;

import java.util.List;

public interface TaskService {
    ResponseDTO createTask(TaskDTO taskDTO);
    List<TaskDTO> getTaskByUser();
}
