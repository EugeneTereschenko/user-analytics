package com.example.activity.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityStatsDTO {
    private long totalActivities;
    private long loginCount;
    private long logoutCount;
    private long createCount;
    private long updateCount;
    private long deleteCount;
    private long viewCount;
}
