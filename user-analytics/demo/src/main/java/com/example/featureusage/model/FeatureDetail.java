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
public class FeatureDetail {
    private String name;
    private String category;
    private Integer users;
    private String avgTime;
    private Double growth;
    private Double rating;
    private Integer sessions;
    private String lastUsed;
}
