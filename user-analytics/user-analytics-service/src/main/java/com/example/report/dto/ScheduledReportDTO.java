/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.report.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduledReportDTO {
    private String id;
    private String name;
    private String templateId;
    private String frequency;
    private List<String> recipients;
    private ReportFilterDTO filters;
    private Boolean enabled;
    private String nextRun;
    private String createdAt;
}
