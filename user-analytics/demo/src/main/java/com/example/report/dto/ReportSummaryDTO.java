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
public class ReportSummaryDTO {
    private Integer totalRecords;
    private Double totalValue;
    private Double averageValue;
    private List<String> highlights;
}
