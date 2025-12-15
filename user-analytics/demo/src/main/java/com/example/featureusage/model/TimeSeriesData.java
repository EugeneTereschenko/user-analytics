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
public class TimeSeriesData {
    private String date;
    private Integer value;
    private String feature;
}
