package com.example.demo.service.impl;

import com.example.demo.dto.AuditDTO;
import com.example.demo.dto.ResponseDTO;
import com.example.demo.model.Audit;

public interface AuditService {
    Audit saveAudit(AuditDTO auditDTO);
    ResponseDTO createAudit(AuditDTO auditDTO);
}
