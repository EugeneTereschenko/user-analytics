/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.report.service;

import com.example.activity.model.Activity;
import com.example.activity.repository.ActivityRepository;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.featureusage.repository.FeatureUsageRepository;
import com.example.demo.repository.UserRepository;
import com.example.featureusage.model.FeatureUsage;
import com.example.report.dto.*;
import com.example.report.model.ReportFrequency;
import com.example.report.model.SavedReport;
import com.example.report.model.ScheduledReport;
import com.example.report.repository.SavedReportRepository;
import com.example.report.repository.ScheduledReportRepository;
import com.example.report.service.impl.ReportsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.report.model.ReportFrequency.DAILY;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportsServiceImpl implements ReportsService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final FeatureUsageRepository featureUsageRepository;
    private final ActivityRepository activityRepository;
    private final com.example.report.repository.SavedReportRepository savedReportRepository;
    private final ScheduledReportRepository scheduledReportRepository;
    private final ObjectMapper objectMapper;

    @Override
    public List<ReportTemplateDTO> getReportTemplates() {
        return Arrays.asList(
                ReportTemplateDTO.builder()
                        .id("user-activity")
                        .name("User Activity Report")
                        .description("Detailed user activity and engagement metrics")
                        .icon("bi-people")
                        .category("user")
                        .build(),
                ReportTemplateDTO.builder()
                        .id("system-usage")
                        .name("System Usage Report")
                        .description("Overall system usage and performance metrics")
                        .icon("bi-bar-chart")
                        .category("usage")
                        .build(),
                ReportTemplateDTO.builder()
                        .id("audit-logs")
                        .name("Audit Log Report")
                        .description("Security and compliance audit trail")
                        .icon("bi-shield-check")
                        .category("system")
                        .build(),
                ReportTemplateDTO.builder()
                        .id("performance")
                        .name("Performance Metrics")
                        .description("System performance and health indicators")
                        .icon("bi-speedometer2")
                        .category("system")
                        .build()
        );
    }

    @Override
    public ReportDataDTO generateReport(String templateId, ReportFilterDTO filters) {
        log.info("Generating report: {}", templateId);

        return switch (templateId) {
            case "user-activity" -> getUserActivityReport(
                    filters.getStartDate(),
                    filters.getEndDate()
            );
            case "system-usage" -> getSystemUsageReport(
                    filters.getStartDate(),
                    filters.getEndDate()
            );
            case "audit-logs" -> getAuditLogReport(filters);
            case "performance" -> getPerformanceReport("weekly");
            default -> throw new IllegalArgumentException("Unknown template: " + templateId);
        };
    }

    @Override
    public ReportDataDTO getUserActivityReport(String startDate, String endDate) {
        LocalDateTime start = parseDate(startDate);
        LocalDateTime end = parseDate(endDate);

        List<Activity> activities = activityRepository.findByTimestampBetween(start, end);

        // Group by user
        Map<String, Long> userActivityCounts = activities.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getUsername(),
                        Collectors.counting()
                ));

        List<Map<String, Object>> reportData = userActivityCounts.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("username", entry.getKey());
                    row.put("activityCount", entry.getValue());
                    row.put("lastActivity", activities.stream()
                            .filter(a -> a.getUsername().equals(entry.getKey()))
                            .max(Comparator.comparing(Activity::getTimestamp))
                            .map(a -> a.getTimestamp().toString())
                            .orElse("N/A"));
                    return row;
                })
                .sorted((a, b) -> ((Long)b.get("activityCount")).compareTo((Long)a.get("activityCount")))
                .collect(Collectors.toList());

        // Calculate summary
        ReportSummaryDTO summary = ReportSummaryDTO.builder()
                .totalRecords(reportData.size())
                .totalValue((double) activities.size())
                .averageValue(activities.size() / (double) Math.max(reportData.size(), 1))
                .highlights(Arrays.asList(
                        String.format("Total activities: %d", activities.size()),
                        String.format("Active users: %d", reportData.size()),
                        String.format("Average activities per user: %.2f",
                                activities.size() / (double) Math.max(reportData.size(), 1))
                ))
                .build();

        return ReportDataDTO.builder()
                .title("User Activity Report")
                .type("user-activity")
                .generatedAt(LocalDateTime.now().toString())
                .data(reportData)
                .summary(summary)
                .build();
    }

    @Override
    public ReportDataDTO getSystemUsageReport(String startDate, String endDate) {
        LocalDateTime start = parseDate(startDate);
        LocalDateTime end = parseDate(endDate);

        List<FeatureUsage> usageData = featureUsageRepository.findByTimestampBetween(start, end);

        Map<String, Long> featureCounts = usageData.stream()
                .collect(Collectors.groupingBy(
                        FeatureUsage::getFeatureName,
                        Collectors.counting()
                ));

        List<Map<String, Object>> reportData = featureCounts.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> row = new HashMap<>();
                    row.put("feature", entry.getKey());
                    row.put("usageCount", entry.getValue());
                    row.put("uniqueUsers", usageData.stream()
                            .filter(u -> u.getFeatureName().equals(entry.getKey()))
                            .map(FeatureUsage::getUserId)
                            .distinct()
                            .count());
                    return row;
                })
                .sorted((a, b) -> ((Long)b.get("usageCount")).compareTo((Long)a.get("usageCount")))
                .collect(Collectors.toList());

        ReportSummaryDTO summary = ReportSummaryDTO.builder()
                .totalRecords(reportData.size())
                .totalValue((double) usageData.size())
                .build();

        return ReportDataDTO.builder()
                .title("System Usage Report")
                .type("system-usage")
                .generatedAt(LocalDateTime.now().toString())
                .data(reportData)
                .summary(summary)
                .build();
    }

    @Override
    public ReportDataDTO getAuditLogReport(ReportFilterDTO filters) {
        // Implementation for audit logs
        return ReportDataDTO.builder()
                .title("Audit Log Report")
                .type("audit-logs")
                .generatedAt(LocalDateTime.now().toString())
                .data(new ArrayList<>())
                .build();
    }

    @Override
    public ReportDataDTO getPerformanceReport(String period) {
        // Implementation for performance metrics
        return ReportDataDTO.builder()
                .title("Performance Report")
                .type("performance")
                .generatedAt(LocalDateTime.now().toString())
                .data(new ArrayList<>())
                .build();
    }

    @Override
    public byte[] exportReport(ReportDataDTO reportData, String format) {
        return switch (format.toLowerCase()) {
            case "csv" -> exportToCsv(reportData);
            case "pdf" -> exportToPdf(reportData);
            case "excel" -> exportToExcel(reportData);
            default -> throw new IllegalArgumentException("Unsupported format: " + format);
        };
    }

    private byte[] exportToCsv(ReportDataDTO reportData) {
        StringBuilder csv = new StringBuilder();

        if (!reportData.getData().isEmpty()) {
            // Headers
            Map<String, Object> first = reportData.getData().get(0);
            csv.append(String.join(",", first.keySet())).append("\n");

            // Data
            for (Map<String, Object> row : reportData.getData()) {
                csv.append(row.values().stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(","))).append("\n");
            }
        }

        return csv.toString().getBytes();
    }

    private byte[] exportToPdf(ReportDataDTO reportData) {
        // Implement PDF generation using iText or similar
        return new byte[0];
    }

    private byte[] exportToExcel(ReportDataDTO reportData) {
        // Implement Excel generation using Apache POI
        return new byte[0];
    }

    @Override
    public SavedReportDTO saveReport(ReportDataDTO reportData) {
        try {
            SavedReport entity = new SavedReport();
            entity.setId(UUID.randomUUID().toString());
            entity.setUserId(getCurrentUserId()); // Get from security context
            entity.setTitle(reportData.getTitle());
            entity.setType(reportData.getType());

            // Convert data to JSON string
            entity.setData(objectMapper.writeValueAsString(reportData.getData()));

            // Convert summary to JSON string if present
            if (reportData.getSummary() != null) {
                entity.setSummary(objectMapper.writeValueAsString(reportData.getSummary()));
            }

            entity.setGeneratedAt(LocalDateTime.parse(reportData.getGeneratedAt()));
            entity.setSavedAt(LocalDateTime.now());

            SavedReport saved = savedReportRepository.save(entity);

            log.info("Report saved successfully: {}", saved.getId());

            return SavedReportDTO.builder()
                    .id(saved.getId())
                    .title(saved.getTitle())
                    .type(saved.getType())
                    .generatedAt(saved.getGeneratedAt().toString())
                    .savedAt(saved.getSavedAt().toString())
                    .summary(reportData.getSummary())
                    .build();

        } catch (JsonProcessingException e) {
            log.error("Error serializing report data", e);
            throw new RuntimeException("Failed to save report", e);
        }
    }



    @Override
    public List<SavedReportDTO> getSavedReports() {
        Long userId = getCurrentUserId();
        List<SavedReport> reports = savedReportRepository.findByUserIdOrderBySavedAtDesc(userId);

        return reports.stream()
                .map(this::convertToSavedReportDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteReport(String id) {
        Long userId = getCurrentUserId();
        savedReportRepository.deleteByIdAndUserId(id, userId);
        log.info("Report deleted: {}", id);
    }

    @Override
    public ScheduledReportDTO scheduleReport(ScheduledReportDTO scheduleDTO) {
        try {
            ScheduledReport entity = new ScheduledReport();
            entity.setId(UUID.randomUUID().toString());
            entity.setUserId(getCurrentUserId());
            entity.setName(scheduleDTO.getName());
            entity.setTemplateId(scheduleDTO.getTemplateId());
            entity.setFrequency(ReportFrequency.valueOf(scheduleDTO.getFrequency().toUpperCase()));

            // Convert List<String> to String[] for entity
            if (scheduleDTO.getRecipients() != null) {
                entity.setRecipients(scheduleDTO.getRecipients().toArray(new String[0]));
            }

            entity.setEnabled(scheduleDTO.getEnabled() != null ? scheduleDTO.getEnabled() : true);

            // Convert filters to JSON string if present
            if (scheduleDTO.getFilters() != null) {
                entity.setFilters(objectMapper.writeValueAsString(scheduleDTO.getFilters()));
            }

            // Calculate next run time
            entity.setNextRun(calculateNextRun(entity.getFrequency()));
            entity.setCreatedAt(LocalDateTime.now());

            ScheduledReport saved = scheduledReportRepository.save(entity);

            log.info("Report scheduled: {}", saved.getId());

            return convertToScheduledReportDTO(saved);

        } catch (JsonProcessingException e) {
            log.error("Error serializing schedule filters", e);
            throw new RuntimeException("Failed to schedule report", e);
        }
    }

    @Override
    public List<ScheduledReportDTO> getScheduledReports() {
        Long userId = getCurrentUserId();
        List<ScheduledReport> schedules = scheduledReportRepository.findByUserIdOrderByCreatedAtDesc(userId);

        return schedules.stream()
                .map(this::convertToScheduledReportDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ScheduledReportDTO updateScheduledReport(String id, ScheduledReportDTO scheduleDTO) {
        try {
            Long userId = getCurrentUserId();
            ScheduledReport entity = scheduledReportRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Scheduled report not found: " + id));

            // Verify ownership
            if (!entity.getUserId().equals(userId)) {
                throw new RuntimeException("Unauthorized access to scheduled report");
            }

            entity.setName(scheduleDTO.getName());
            entity.setTemplateId(scheduleDTO.getTemplateId());
            entity.setFrequency(ReportFrequency.valueOf(scheduleDTO.getFrequency().toUpperCase()));

            // Convert List<String> to String[] for entity
            if (scheduleDTO.getRecipients() != null) {
                entity.setRecipients(scheduleDTO.getRecipients().toArray(new String[0]));
            }

            entity.setEnabled(scheduleDTO.getEnabled());

            if (scheduleDTO.getFilters() != null) {
                entity.setFilters(objectMapper.writeValueAsString(scheduleDTO.getFilters()));
            }

            // Recalculate next run if frequency changed
            entity.setNextRun(calculateNextRun(entity.getFrequency()));
            entity.setUpdatedAt(LocalDateTime.now());

            ScheduledReport updated = scheduledReportRepository.save(entity);

            log.info("Scheduled report updated: {}", id);

            return convertToScheduledReportDTO(updated);

        } catch (JsonProcessingException e) {
            log.error("Error serializing schedule filters", e);
            throw new RuntimeException("Failed to update scheduled report", e);
        }
    }

    @Override
    public void deleteScheduledReport(String id) {
        Long userId = getCurrentUserId();
        scheduledReportRepository.deleteByIdAndUserId(id, userId);
        log.info("Scheduled report deleted: {}", id);
    }

    private LocalDateTime parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return LocalDateTime.now().minusDays(30);
        }
        return LocalDate.parse(dateStr).atStartOfDay();
    }


    private SavedReportDTO convertToSavedReportDTO(SavedReport entity) {
        try {
            ReportSummaryDTO summary = null;
            if (entity.getSummary() != null) {
                summary = objectMapper.readValue(entity.getSummary(), ReportSummaryDTO.class);
            }

            return SavedReportDTO.builder()
                    .id(entity.getId())
                    .title(entity.getTitle())
                    .type(entity.getType())
                    .generatedAt(entity.getGeneratedAt().toString())
                    .savedAt(entity.getSavedAt().toString())
                    .summary(summary)
                    .build();

        } catch (JsonProcessingException e) {
            log.error("Error deserializing saved report", e);
            throw new RuntimeException("Failed to convert saved report", e);
        }
    }

    private ScheduledReportDTO convertToScheduledReportDTO(ScheduledReport entity) {
        try {
            ReportFilterDTO filters = null;
            if (entity.getFilters() != null) {
                filters = objectMapper.readValue(entity.getFilters(), ReportFilterDTO.class);
            }

            // Convert String[] to List<String> for DTO
            List<String> recipients = entity.getRecipients() != null
                    ? Arrays.asList(entity.getRecipients())
                    : new ArrayList<>();

            return ScheduledReportDTO.builder()
                    .id(entity.getId())
                    .name(entity.getName())
                    .templateId(entity.getTemplateId())
                    .frequency(entity.getFrequency().name().toLowerCase())
                    .recipients(recipients)
                    .filters(filters)
                    .enabled(entity.getEnabled())
                    .nextRun(entity.getNextRun() != null ? entity.getNextRun().toString() : null)
                    .createdAt(entity.getCreatedAt().toString())
                    .build();

        } catch (JsonProcessingException e) {
            log.error("Error deserializing scheduled report", e);
            throw new RuntimeException("Failed to convert scheduled report", e);
        }
    }

    private LocalDateTime calculateNextRun(ReportFrequency frequency) {
        LocalDateTime now = LocalDateTime.now();

        return switch (frequency) {
            case DAILY -> now.plusDays(1).withHour(9).withMinute(0).withSecond(0);
            case WEEKLY -> now.plusWeeks(1).with(DayOfWeek.MONDAY).withHour(9).withMinute(0).withSecond(0);
            case MONTHLY -> now.plusMonths(1).withDayOfMonth(1).withHour(9).withMinute(0).withSecond(0);
        };
    }

    private Long getCurrentUserId() {
        return userService.getAuthenticatedUser()
                .map(User::getId)
                .orElseThrow(() -> new RuntimeException("User not authenticated"));
    }
}
