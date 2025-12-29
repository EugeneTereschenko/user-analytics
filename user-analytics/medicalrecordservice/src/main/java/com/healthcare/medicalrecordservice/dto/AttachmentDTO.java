/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.medicalrecordservice.dto;

import com.healthcare.medicalrecordservice.entity.AttachmentType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
class AttachmentDTO {

    private Long id;

    @NotBlank(message = "File name is required")
    private String fileName;

    private String filePath;

    private String fileType;

    private Long fileSize;

    private AttachmentType attachmentType;

    private String description;

    private String uploadedBy;

    private LocalDateTime uploadedAt;
}
