package com.example.demo.service.impl;

import com.example.demo.dto.StatusDTO;
import com.example.demo.model.Status;

import java.util.List;

public interface StatusService {
    StatusDTO getLastStatus();
    List<StatusDTO> getAllStatus();
    Status saveStatus(StatusDTO statusDTO);
}
