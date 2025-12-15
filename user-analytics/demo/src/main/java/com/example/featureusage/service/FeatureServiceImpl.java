
/*
 * © ${year} Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.featureusage.service;

import com.example.demo.repository.FeatureRatingRepository;
import com.example.demo.repository.FeatureUsageRepository;
import com.example.demo.repository.UserRepository;
import com.example.featureusage.dto.CategoryUsageDTO;
import com.example.featureusage.dto.FeatureUsageDTO;
import com.example.featureusage.model.*;
import com.example.featureusage.service.impl.FeatureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeatureServiceImpl implements FeatureService {

    private final FeatureUsageRepository featureUsageRepository;
    private final FeatureRatingRepository featureRatingRepository;
    private final UserRepository userRepository;

    @Override
    public FeatureUsageDTO getFeatureUsage(String period) {
        log.info("Getting feature usage for period: {}", period);
        LocalDateTime startDate = getStartDateForPeriod(period);

        List<Object[]> usageStats = featureUsageRepository.getFeatureUsageStats(startDate);
        List<String> features = new ArrayList<>();
        List<Integer> usageCounts = new ArrayList<>();

        for (Object[] stat : usageStats) {
            features.add((String) stat[0]);
            usageCounts.add(((Long) stat[1]).intValue());
        }

        return FeatureUsageDTO.builder()
                .features(features)
                .usageCounts(usageCounts)
                .build();
    }

    @Override
    public UsageStatistics getStatistics() {
        log.info("Getting overall usage statistics");

        Long totalUsers = userRepository.count();
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        Long activeUsers = featureUsageRepository.countActiveUsers(oneWeekAgo);

        Double engagementRate = totalUsers > 0 ?
                (activeUsers.doubleValue() / totalUsers.doubleValue()) * 100 : 0.0;

        Double avgRating = featureRatingRepository.getAverageRating();
        if (avgRating == null) avgRating = 0.0;

        Long totalSessions = featureUsageRepository.countTotalSessions();
        Double avgDuration = featureUsageRepository.getAverageSessionDuration();
        if (avgDuration == null) avgDuration = 0.0;

        String avgSessionDuration = formatDuration(avgDuration.intValue());

        return UsageStatistics.builder()
                .totalUsers(totalUsers.intValue())
                .activeUsers(activeUsers.intValue())
                .engagementRate(engagementRate)
                .avgRating(avgRating)
                .totalSessions(totalSessions.intValue())
                .avgSessionDuration(avgSessionDuration)
                .build();
    }

    @Override
    public List<FeatureDetail> getFeatureDetails() {
        log.info("Getting detailed feature metrics");

        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        LocalDateTime twoWeeksAgo = LocalDateTime.now().minusWeeks(2);

        // Get current week stats
        List<Object[]> currentStats = featureUsageRepository.getFeatureUsageStats(oneWeekAgo);
        List<Object[]> previousStats = featureUsageRepository.getFeatureUsageStats(twoWeeksAgo);

        // Get unique users per feature
        List<Object[]> usersPerFeature = featureUsageRepository.getUniqueUsersPerFeature(oneWeekAgo);
        Map<String, Long> userMap = new HashMap<>();
        for (Object[] data : usersPerFeature) {
            userMap.put((String) data[0], (Long) data[1]);
        }

        // Get average time per feature
        List<Object[]> avgTimeData = featureUsageRepository.getAvgTimePerFeature();
        Map<String, Double> avgTimeMap = new HashMap<>();
        for (Object[] data : avgTimeData) {
            avgTimeMap.put((String) data[0], (Double) data[1]);
        }

        // Get ratings per feature
        List<Object[]> ratingsData = featureRatingRepository.getAverageRatingPerFeature();
        Map<String, Double> ratingsMap = new HashMap<>();
        for (Object[] data : ratingsData) {
            ratingsMap.put((String) data[0], (Double) data[1]);
        }

        // Calculate growth rate
        Map<String, Long> currentMap = new HashMap<>();
        for (Object[] data : currentStats) {
            currentMap.put((String) data[0], (Long) data[1]);
        }

        Map<String, Long> previousMap = new HashMap<>();
        for (Object[] data : previousStats) {
            previousMap.put((String) data[0], (Long) data[1]);
        }

        List<FeatureDetail> details = new ArrayList<>();
        for (Map.Entry<String, Long> entry : currentMap.entrySet()) {
            String featureName = entry.getKey();
            Long currentCount = entry.getValue();
            Long previousCount = previousMap.getOrDefault(featureName, 0L);

            Double growth = previousCount > 0 ?
                    ((double) (currentCount - previousCount) / previousCount) * 100 : 0.0;


            details.add(FeatureDetail.builder()
                    .name(featureName)
                    .category(getFeatureCategory(featureName))
                    .users(userMap.getOrDefault(featureName, 0L).intValue())
                    .avgTime(formatDuration(avgTimeMap.getOrDefault(featureName, 0.0).intValue()))
                    .growth(growth)
                    .rating(ratingsMap.getOrDefault(featureName, 0.0))
                    .sessions(currentCount.intValue())
                    .lastUsed(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                    .build());
        }

        return details.stream()
                .sorted((a, b) -> b.getUsers().compareTo(a.getUsers()))
                .collect(Collectors.toList());
    }

    @Override
    public UsageTrend getUsageTrends(String period, String feature) {
        log.info("Getting usage trends for period: {} and feature: {}", period, feature);

        LocalDateTime startDate = getStartDateForPeriod(period);
        List<FeatureUsage> usageData;

        if (feature != null && !feature.isEmpty()) {
            usageData = featureUsageRepository.findByFeatureName(feature)
                    .stream()
                    .filter(u -> u.getTimestamp().isAfter(startDate))
                    .collect(Collectors.toList());
        } else {
            usageData = featureUsageRepository.findByTimestampBetween(startDate, LocalDateTime.now());
        }

        // Group by date
        Map<String, Integer> dailyCounts = new TreeMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (FeatureUsage usage : usageData) {
            String dateKey = usage.getTimestamp().format(formatter);
            dailyCounts.put(dateKey, dailyCounts.getOrDefault(dateKey, 0) + 1);
        }

        List<TimeSeriesData> trendData = dailyCounts.entrySet().stream()
                .map(entry -> TimeSeriesData.builder()
                        .date(entry.getKey())
                        .value(entry.getValue())
                        .feature(feature)
                        .build())
                .collect(Collectors.toList());

        return UsageTrend.builder()
                .period(period)
                .data(trendData)
                .build();
    }

    @Override
    public List<FeatureDetail> getTopFeatures(int limit) {
        log.info("Getting top {} features", limit);
        return getFeatureDetails().stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryUsageDTO getUsageByCategory() {
        log.info("Getting usage by category");

        List<Object[]> categoryData = featureUsageRepository.getUsageByCategory();
        List<String> categories = new ArrayList<>();
        List<Integer> values = new ArrayList<>();

        for (Object[] data : categoryData) {
            String category = (String) data[0];
            if (category == null) category = "Uncategorized";
            categories.add(category);
            values.add(((Long) data[1]).intValue());
        }

        return CategoryUsageDTO.builder()
                .categories(categories)
                .values(values)
                .build();
    }

    @Transactional
    @Override
    public void trackFeatureUsage(String featureName, Long userId, Integer duration) {
        log.info("Tracking feature usage: {} for user: {}", featureName, userId);

        FeatureUsage usage = new FeatureUsage();
        usage.setUserId(userId);
        usage.setFeatureName(featureName);
        usage.setCategory(getFeatureCategory(featureName));
        usage.setDurationSeconds(duration);
        usage.setSessionId(UUID.randomUUID().toString());

        featureUsageRepository.save(usage);
    }

    private LocalDateTime getStartDateForPeriod(String period) {
        LocalDateTime now = LocalDateTime.now();
        return switch (period.toLowerCase()) {
            case "daily" -> now.minusDays(1);
            case "weekly" -> now.minusWeeks(1);
            case "monthly" -> now.minusMonths(1);
            case "yearly" -> now.minusYears(1);
            default -> now.minusWeeks(1);
        };
    }

    private String getFeatureCategory(String featureName) {
        // Map feature names to categories
        Map<String, String> categoryMap = new HashMap<>();
        categoryMap.put("Dashboard", "Analytics");
        categoryMap.put("Reports", "Analytics");
        categoryMap.put("Analytics", "Analytics");
        categoryMap.put("Settings", "Configuration");
        categoryMap.put("Profile", "User Management");
        categoryMap.put("Users", "User Management");
        categoryMap.put("Tasks", "Productivity");
        categoryMap.put("Calendar", "Productivity");

        return categoryMap.getOrDefault(featureName, "Other");
    }

    private String formatDuration(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%dm %ds", minutes, remainingSeconds);
    }
}
