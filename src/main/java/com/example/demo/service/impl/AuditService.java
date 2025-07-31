package com.example.demo.service.impl;

import com.example.demo.dto.AuditDTO;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.model.Audit;

import java.util.List;

public interface AuditService {
    Audit saveAudit(AuditDTO auditDTO);
    ResponseDTO createAudit(AuditDTO auditDTO);
    List<AuditDTO> getAllAudits();
    List<AuditDTO> getAuditsByUser(String username);
}
