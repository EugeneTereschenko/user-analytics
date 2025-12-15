/*
 * © ${year} Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.featureusage.service.impl;

import com.example.featureusage.dto.CategoryUsageDTO;
import com.example.featureusage.dto.FeatureUsageDTO;
import com.example.featureusage.model.FeatureDetail;
import com.example.featureusage.model.UsageStatistics;
import com.example.featureusage.model.UsageTrend;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FeatureService {
    FeatureUsageDTO getFeatureUsage(String period);

    UsageStatistics getStatistics();

    List<FeatureDetail> getFeatureDetails();

    UsageTrend getUsageTrends(String period, String feature);

    List<FeatureDetail> getTopFeatures(int limit);

    CategoryUsageDTO getUsageByCategory();

    @Transactional
    void trackFeatureUsage(String featureName, Long userId, Integer duration);
}
