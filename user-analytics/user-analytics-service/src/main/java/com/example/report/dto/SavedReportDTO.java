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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavedReportDTO {
    private String id;
    private String title;
    private String type;
    private String generatedAt;
    private String savedAt;
    private ReportSummaryDTO summary;
}
