/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.example.report.repository;

import com.example.demo.UserAnalyticsJavaApplication;
import com.example.demo.dto.UserEventDTO;
import com.example.demo.testutil.TestcontainersConfiguration;
import com.example.report.model.SavedReport;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {UserAnalyticsJavaApplication.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
class SavedReportRepositoryTest {

    @Autowired
    private SavedReportRepository savedReportRepository;

    @MockBean
    private KafkaTemplate<String, UserEventDTO> kafkaTemplate;

    @Test
    void testSaveAndFindById() {
        SavedReport report = new SavedReport();
        report.setId(UUID.randomUUID().toString());
        report.setUserId(1L);
        report.setTitle("Test Report");
        report.setType("SUMMARY");
        report.setData("{}");
        report.setGeneratedAt(LocalDateTime.now());
        report.setSavedAt(LocalDateTime.now());

        savedReportRepository.save(report);

        Optional<SavedReport> found = savedReportRepository.findById(report.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Test Report");
    }

    @Test
    void testFindAll() {
        SavedReport report1 = new SavedReport();
        report1.setId(UUID.randomUUID().toString());
        report1.setUserId(1L);
        report1.setTitle("Report 1");
        report1.setType("SUMMARY");
        report1.setData("{}");

        report1.setGeneratedAt(LocalDateTime.now());
        report1.setSavedAt(LocalDateTime.now());

        SavedReport report2 = new SavedReport();
        report2.setId(UUID.randomUUID().toString());
        report2.setUserId(2L);
        report2.setTitle("Report 2");
        report2.setType("DETAIL");
        report2.setData("{}");
        report2.setGeneratedAt(LocalDateTime.now());
        report2.setSavedAt(LocalDateTime.now());

        savedReportRepository.save(report1);
        savedReportRepository.save(report2);

        assertThat(savedReportRepository.findAll()).hasSize(2);
    }
}
