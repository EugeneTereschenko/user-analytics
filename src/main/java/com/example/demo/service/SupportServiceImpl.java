package com.example.demo.service;

import com.example.demo.dto.ResponseDTO;
import com.example.demo.dto.SupportDTO;
import com.example.demo.model.Support;
import com.example.demo.repository.SupportRepository;
import com.example.demo.service.impl.SupportService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SupportServiceImpl implements SupportService {

    private final SupportRepository supportRepository;

    public ResponseDTO createSupport(SupportDTO supportDTO) {
        if (supportRepository.findBySubject(supportDTO.getSubject()).isEmpty()) {
            supportRepository.save(new Support.Builder()
                    .subject(supportDTO.getSubject())
                    .message(supportDTO.getMessage())
                    .successMessage("Support created successfully")
                    .build());
        }
        return ResponseDTO.builder()
                .message("Support request created successfully")
                .status("success")
                .build();
    }
}
