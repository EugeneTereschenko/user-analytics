/*
 * © ${year} Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.featureusage.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsageStatistics {
    private Integer totalUsers;
    private Integer activeUsers;
    private Double engagementRate;
    private Double avgRating;
    private Integer totalSessions;
    private String avgSessionDuration;
}
