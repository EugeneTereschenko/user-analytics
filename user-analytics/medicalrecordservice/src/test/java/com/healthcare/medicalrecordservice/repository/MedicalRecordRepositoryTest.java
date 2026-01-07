/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.medicalrecordservice.repository;

import com.example.common.security.client.AuthServiceClient;
import com.healthcare.medicalrecordservice.MedicalRecordServiceApplication;
import com.healthcare.medicalrecordservice.entity.MedicalRecord;
import com.healthcare.medicalrecordservice.entity.RecordType;
import com.healthcare.medicalrecordservice.testutil.MedicalRecordTestBuilder;
import com.healthcare.medicalrecordservice.testutil.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {MedicalRecordServiceApplication.class})
@Import({TestcontainersConfiguration.class, FeignAutoConfiguration.class})
class MedicalRecordRepositoryTest {


    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @MockBean
    private AuthServiceClient authServiceClient;

    @Test
    void shouldSaveAndFindByPatientId() {
        MedicalRecord record = new MedicalRecordTestBuilder()
                .withPatientId(100L)
                .withDoctorId(200L)
                .withRecordType(RecordType.CONSULTATION)
                .build();
        medicalRecordRepository.save(record);

        Page<MedicalRecord> found = medicalRecordRepository.findByPatientId(100L, PageRequest.of(0, 10));
        assertThat(found.getContent()).hasSize(1);
        assertThat(found.getContent().get(0).getPatientId()).isEqualTo(100L);
    }

    @Test
    @WithMockUser
    void shouldFindByDoctorId() {
        MedicalRecord record = new MedicalRecordTestBuilder()
                .withPatientId(101L)
                .withDoctorId(201L)
                .build();
        medicalRecordRepository.save(record);

        Page<MedicalRecord> found = medicalRecordRepository.findByDoctorId(201L, PageRequest.of(0, 10));
        assertThat(found.getContent()).hasSize(1);
        assertThat(found.getContent().get(0).getDoctorId()).isEqualTo(201L);
    }

    @Test
    @WithMockUser
    void shouldFindByRecordType() {
        MedicalRecord record = new MedicalRecordTestBuilder()
                .withRecordType(RecordType.LAB_RESULT)
                .build();
        medicalRecordRepository.save(record);

        Page<MedicalRecord> found = medicalRecordRepository.findByRecordType(RecordType.LAB_RESULT, PageRequest.of(0, 10));
        assertThat(found.getContent()).extracting(MedicalRecord::getRecordType).contains(RecordType.LAB_RESULT);
    }

}