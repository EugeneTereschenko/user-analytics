/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.medicalrecordservice.service;

import com.example.common.security.util.SecurityUtils;
import com.healthcare.medicalrecordservice.dto.MedicalRecordDTO;
import com.healthcare.medicalrecordservice.entity.MedicalRecord;
import com.healthcare.medicalrecordservice.testutil.MedicalRecordTestBuilder;
import com.healthcare.medicalrecordservice.entity.RecordStatus;
import com.healthcare.medicalrecordservice.exception.MedicalRecordNotFoundException;
import com.healthcare.medicalrecordservice.mapper.MedicalRecordMapper;
import com.healthcare.medicalrecordservice.repository.MedicalRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MedicalRecord Service Tests")
class MedicalRecordServiceImplTest {

    @Mock
    private MedicalRecordRepository medicalRecordRepository;
    @Mock
    private MedicalRecordMapper medicalRecordMapper;

    @InjectMocks
    private MedicalRecordServiceImpl medicalRecordService;

    private MedicalRecord record;
    private MedicalRecordDTO recordDTO;

    @BeforeEach
    void setUp() {
        record = new MedicalRecordTestBuilder().withId(1L).build();
        recordDTO = mock(MedicalRecordDTO.class);
    }

    @Test
    void shouldCreateMedicalRecord() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserId).thenReturn(Optional.of(1L));
            when(medicalRecordMapper.toEntity(recordDTO)).thenReturn(record);
            when(medicalRecordRepository.save(record)).thenReturn(record);
            when(medicalRecordMapper.toDTO(record)).thenReturn(recordDTO);

            MedicalRecordDTO result = medicalRecordService.createMedicalRecord(recordDTO);

            assertThat(result).isEqualTo(recordDTO);
            verify(medicalRecordRepository).save(record);
        }
    }

    @Test
    void shouldGetMedicalRecordById() {
        when(medicalRecordRepository.findById(1L)).thenReturn(Optional.of(record));
        when(medicalRecordMapper.toDTO(record)).thenReturn(recordDTO);

        MedicalRecordDTO result = medicalRecordService.getMedicalRecordById(1L);

        assertThat(result).isEqualTo(recordDTO);
    }

    @Test
    void shouldThrowWhenMedicalRecordNotFound() {
        when(medicalRecordRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> medicalRecordService.getMedicalRecordById(2L))
                .isInstanceOf(MedicalRecordNotFoundException.class);
    }

    @Test
    void shouldGetAllMedicalRecords() {
        Page<MedicalRecord> page = new PageImpl<>(List.of(record));
        when(medicalRecordRepository.findAll(any(PageRequest.class))).thenReturn(page);
        when(medicalRecordMapper.toDTO(record)).thenReturn(recordDTO);

        Page<MedicalRecordDTO> result = medicalRecordService.getAllMedicalRecords(PageRequest.of(0, 10));

        assertThat(result.getContent()).containsExactly(recordDTO);
    }

    @Test
    void shouldUpdateMedicalRecord() {
        when(medicalRecordRepository.findById(1L)).thenReturn(Optional.of(record));
        doNothing().when(medicalRecordMapper).updateEntityFromDTO(recordDTO, record);
        when(medicalRecordRepository.save(record)).thenReturn(record);
        when(medicalRecordMapper.toDTO(record)).thenReturn(recordDTO);

        MedicalRecordDTO result = medicalRecordService.updateMedicalRecord(1L, recordDTO);

        assertThat(result).isEqualTo(recordDTO);
    }

    @Test
    void shouldFinalizeMedicalRecord() {
        when(medicalRecordRepository.findById(1L)).thenReturn(Optional.of(record));
        when(medicalRecordRepository.save(any())).thenReturn(record);
        when(medicalRecordMapper.toDTO(record)).thenReturn(recordDTO);

        MedicalRecordDTO result = medicalRecordService.finalizeMedicalRecord(1L);

        assertThat(result).isEqualTo(recordDTO);
        assertThat(record.getStatus()).isEqualTo(RecordStatus.FINALIZED);
    }

    @Test
    void shouldSignMedicalRecord() {
        when(medicalRecordRepository.findById(1L)).thenReturn(Optional.of(record));
        when(medicalRecordRepository.save(any())).thenReturn(record);
        when(medicalRecordMapper.toDTO(record)).thenReturn(recordDTO);

        MedicalRecordDTO result = medicalRecordService.signMedicalRecord(1L, "Dr. Test");

        assertThat(result).isEqualTo(recordDTO);
        assertThat(record.getStatus()).isEqualTo(RecordStatus.SIGNED);
        assertThat(record.getSignedBy()).isEqualTo("Dr. Test");
        assertThat(record.getSignedAt()).isNotNull();
    }

    @Test
    void shouldArchiveMedicalRecord() {
        when(medicalRecordRepository.findById(1L)).thenReturn(Optional.of(record));
        when(medicalRecordRepository.save(any())).thenReturn(record);
        when(medicalRecordMapper.toDTO(record)).thenReturn(recordDTO);

        MedicalRecordDTO result = medicalRecordService.archiveMedicalRecord(1L);

        assertThat(result).isEqualTo(recordDTO);
        assertThat(record.getStatus()).isEqualTo(RecordStatus.ARCHIVED);
    }

    @Test
    void shouldDeleteMedicalRecord() {
        when(medicalRecordRepository.existsById(1L)).thenReturn(true);
        doNothing().when(medicalRecordRepository).deleteById(1L);

        medicalRecordService.deleteMedicalRecord(1L);

        verify(medicalRecordRepository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeleteMedicalRecordNotFound() {
        when(medicalRecordRepository.existsById(2L)).thenReturn(false);

        assertThatThrownBy(() -> medicalRecordService.deleteMedicalRecord(2L))
                .isInstanceOf(MedicalRecordNotFoundException.class);
    }
}
