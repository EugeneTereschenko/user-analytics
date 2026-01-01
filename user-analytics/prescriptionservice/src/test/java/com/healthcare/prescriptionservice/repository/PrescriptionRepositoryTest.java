/*
 * © 2026 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.prescriptionservice.repository;

import com.healthcare.prescriptionservice.PrescriptionServiceApplication;
import com.healthcare.prescriptionservice.entity.Prescription;
import com.healthcare.prescriptionservice.entity.PrescriptionStatus;
import com.healthcare.prescriptionservice.testutil.MedicationTestBuilder;
import com.healthcare.prescriptionservice.testutil.PrescriptionTestBuilder;
import com.healthcare.prescriptionservice.testutil.TestcontainersConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {PrescriptionServiceApplication.class})
@Import(TestcontainersConfiguration.class)
class PrescriptionRepositoryTest {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Test
    @DisplayName("Should find prescription by prescription number")
    void shouldFindByPrescriptionNumber() {
        Prescription prescription = PrescriptionTestBuilder.defaultBuilder()
                .withPrescriptionNumber("RX-12345")
                .withMedications(List.of(MedicationTestBuilder.defaultBuilder().build()))
                .build();
        prescriptionRepository.save(prescription);

        Optional<Prescription> found = prescriptionRepository.findByPrescriptionNumber("RX-12345");
        assertThat(found).isPresent();
        assertThat(found.get().getPrescriptionNumber()).isEqualTo("RX-12345");
    }

    @Test
    @DisplayName("Should check existence by prescription number")
    void shouldCheckExistsByPrescriptionNumber() {
        Prescription prescription = PrescriptionTestBuilder.defaultBuilder()
                .withPrescriptionNumber("RX-54321")
                .withMedications(List.of(MedicationTestBuilder.defaultBuilder().build()))
                .build();
        prescriptionRepository.save(prescription);

        boolean exists = prescriptionRepository.existsByPrescriptionNumber("RX-54321");
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should find expired prescriptions")
    void shouldFindExpiredPrescriptions() {

        Prescription expired = PrescriptionTestBuilder.defaultBuilder()
                .withStatus(PrescriptionStatus.ACTIVE)
                .withValidUntil(LocalDate.now().minusDays(1))
                .withValidUntil(LocalDate.now().minusDays(1))
                .withMedications(List.of(MedicationTestBuilder.defaultBuilder().build()))
                .build();
        prescriptionRepository.save(expired);

        List<Prescription> expiredList = prescriptionRepository.findExpiredPrescriptions(LocalDate.now());
        assertThat(expiredList).extracting(Prescription::getId).contains(expired.getId());
    }
}
