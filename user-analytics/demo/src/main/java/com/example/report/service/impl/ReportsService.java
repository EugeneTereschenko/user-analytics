/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.report.service.impl;

import com.example.report.dto.*;

import java.util.List;

public interface ReportsService {

    List<ReportTemplateDTO> getReportTemplates();

    ReportDataDTO generateReport(String templateId, ReportFilterDTO filters);

    ReportDataDTO getUserActivityReport(String startDate, String endDate);

    ReportDataDTO getSystemUsageReport(String startDate, String endDate);

    ReportDataDTO getAuditLogReport(ReportFilterDTO filters);

    ReportDataDTO getPerformanceReport(String period);

    byte[] exportReport(ReportDataDTO reportData, String format);

    SavedReportDTO saveReport(ReportDataDTO reportData);

    List<SavedReportDTO> getSavedReports();

    void deleteReport(String id);

    ScheduledReportDTO scheduleReport(ScheduledReportDTO schedule);

    List<ScheduledReportDTO> getScheduledReports();

    ScheduledReportDTO updateScheduledReport(String id, ScheduledReportDTO schedule);

    void deleteScheduledReport(String id);
}
