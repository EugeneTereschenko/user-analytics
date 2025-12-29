/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.medicalrecordservice.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "lab_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_record_id", nullable = false)
    private MedicalRecord medicalRecord;

    @Column(name = "test_name", nullable = false, length = 200)
    private String testName;

    @Column(name = "test_code", length = 50)
    private String testCode;

    @Column(name = "result_value", length = 200)
    private String resultValue;

    @Column(name = "unit", length = 50)
    private String unit;

    @Column(name = "reference_range", length = 100)
    private String referenceRange;

    @Column(name = "is_abnormal")
    private Boolean isAbnormal = false;

    @Column(columnDefinition = "TEXT")
    private String interpretation;

    @Column(name = "test_date", nullable = false)
    private LocalDate testDate;

    @Column(name = "result_date")
    private LocalDate resultDate;

    @Column(name = "laboratory_name", length = 200)
    private String laboratoryName;

    @Column(name = "performed_by", length = 200)
    private String performedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LabStatus status = LabStatus.PENDING;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
