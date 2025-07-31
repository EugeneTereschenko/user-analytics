package com.example.demo.service;

import com.example.demo.dto.StatusDTO;
import com.example.demo.model.Status;
import com.example.demo.repository.StatusRepository;
import com.example.demo.service.impl.StatusService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;

    public List<StatusDTO> getAllStatus() {
        return statusRepository.findAll().stream()
                .map(status -> new StatusDTO.Builder()
                        .uptime(status.getUptime())
                        .cpu(status.getCpu())
                        .memory(status.getMemory())
                        .apiLatency(status.getApiLatency())
                        .jobs(status.getJobs())
                        .build())
                .collect(Collectors.toList());
    }

    public StatusDTO getLastStatus() {
        var status = statusRepository.findTopByOrderByIdDesc();
        if (status == null) {
            return null;
        }
        return new StatusDTO.Builder()
                .uptime(status.getUptime())
                .cpu(status.getCpu())
                .memory(status.getMemory())
                .apiLatency(status.getApiLatency())
                .jobs(status.getJobs())
                .build();
    }

    public Status saveStatus(StatusDTO statusDTO) {
        Status status = Status.builder()
                .uptime(statusDTO.getUptime())
                .cpu(statusDTO.getCpu())
                .memory(statusDTO.getMemory())
                .apiLatency(statusDTO.getApiLatency())
                .jobs(statusDTO.getJobs())
                .build();
        return statusRepository.save(status);
    }
}
