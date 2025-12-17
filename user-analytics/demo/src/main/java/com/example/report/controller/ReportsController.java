/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.report.controller;

import com.example.report.dto.*;
import com.example.report.service.impl.ReportsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reports")
@CrossOrigin(origins = "http://localhost:4200")
public class ReportsController {

    private final ReportsService reportsService;

    @GetMapping("/templates")
    public ResponseEntity<List<ReportTemplateDTO>> getReportTemplates() {
        log.info("GET /api/reports/templates");
        return ResponseEntity.ok(reportsService.getReportTemplates());
    }

    @PostMapping("/generate")
    public ResponseEntity<ReportDataDTO> generateReport(@RequestBody GenerateReportRequest request) {
        log.info("POST /api/reports/generate - Template: {}", request.getTemplateId());
        return ResponseEntity.ok(reportsService.generateReport(
                request.getTemplateId(),
                request.getFilters()
        ));
    }

    @GetMapping("/user-activity")
    public ResponseEntity<ReportDataDTO> getUserActivityReport(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        log.info("GET /api/reports/user-activity");
        return ResponseEntity.ok(reportsService.getUserActivityReport(startDate, endDate));
    }

    @GetMapping("/system-usage")
    public ResponseEntity<ReportDataDTO> getSystemUsageReport(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        log.info("GET /api/reports/system-usage");
        return ResponseEntity.ok(reportsService.getSystemUsageReport(startDate, endDate));
    }

    @PostMapping("/audit-logs")
    public ResponseEntity<ReportDataDTO> getAuditLogReport(@RequestBody ReportFilterDTO filters) {
        log.info("POST /api/reports/audit-logs");
        return ResponseEntity.ok(reportsService.getAuditLogReport(filters));
    }

    @GetMapping("/performance")
    public ResponseEntity<ReportDataDTO> getPerformanceReport(
            @RequestParam(defaultValue = "weekly") String period) {
        log.info("GET /api/reports/performance?period={}", period);
        return ResponseEntity.ok(reportsService.getPerformanceReport(period));
    }

    @PostMapping("/export")
    public ResponseEntity<byte[]> exportReport(@RequestBody ExportReportRequest request) {
        log.info("POST /api/reports/export - Format: {}", request.getFormat());

        byte[] data = reportsService.exportReport(request.getReportData(), request.getFormat());
        String filename = String.format("report_%s.%s",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")),
                request.getFormat());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                .contentType(getMediaType(request.getFormat()))
                .body(data);
    }

    @PostMapping("/save")
    public ResponseEntity<SavedReportDTO> saveReport(@RequestBody ReportDataDTO reportData) {
        log.info("POST /api/reports/save");
        return ResponseEntity.ok(reportsService.saveReport(reportData));
    }

    @GetMapping("/saved")
    public ResponseEntity<List<SavedReportDTO>> getSavedReports() {
        log.info("GET /api/reports/saved");
        return ResponseEntity.ok(reportsService.getSavedReports());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable String id) {
        log.info("DELETE /api/reports/{}", id);
        reportsService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/schedule")
    public ResponseEntity<ScheduledReportDTO> scheduleReport(@RequestBody ScheduledReportDTO schedule) {
        log.info("POST /api/reports/schedule");
        return ResponseEntity.ok(reportsService.scheduleReport(schedule));
    }

    @GetMapping("/scheduled")
    public ResponseEntity<List<ScheduledReportDTO>> getScheduledReports() {
        log.info("GET /api/reports/scheduled");
        return ResponseEntity.ok(reportsService.getScheduledReports());
    }

    @PutMapping("/scheduled/{id}")
    public ResponseEntity<ScheduledReportDTO> updateScheduledReport(
            @PathVariable String id,
            @RequestBody ScheduledReportDTO schedule) {
        log.info("PUT /api/reports/scheduled/{}", id);
        return ResponseEntity.ok(reportsService.updateScheduledReport(id, schedule));
    }

    @DeleteMapping("/scheduled/{id}")
    public ResponseEntity<Void> deleteScheduledReport(@PathVariable String id) {
        log.info("DELETE /api/reports/scheduled/{}", id);
        reportsService.deleteScheduledReport(id);
        return ResponseEntity.noContent().build();
    }

    private MediaType getMediaType(String format) {
        return switch (format.toLowerCase()) {
            case "pdf" -> MediaType.APPLICATION_PDF;
            case "excel" -> MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            case "csv" -> MediaType.parseMediaType("text/csv");
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };
    }
}
