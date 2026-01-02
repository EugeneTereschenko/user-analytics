/*
 * © ${year} Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.featureusage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureUsageDTO {
    private List<String> features;
    private List<Integer> usageCounts;
    private List<String> timestamps;
    private List<String> categories;
    private List<Double> avgTimeSpent;
    private List<Double> growthRate;
    private List<Double> ratings;
    private List<Integer> activeUsers;
}
