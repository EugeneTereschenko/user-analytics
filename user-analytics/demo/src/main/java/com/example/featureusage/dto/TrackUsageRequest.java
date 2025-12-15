/*
 * © ${year} Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.featureusage.dto;

import lombok.Data;

@Data
public class TrackUsageRequest {
    private String featureName;
    private Long userId;
    private Integer duration;
}
