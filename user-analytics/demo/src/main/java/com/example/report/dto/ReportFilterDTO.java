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
public class ReportFilterDTO {
    private String startDate;
    private String endDate;
    private String userId;
    private String actionType;
    private List<String> categories;
}
