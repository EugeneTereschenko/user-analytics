/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.billingservice.dto;

import com.example.common.security.util.SecurityUtils;
import com.healthcare.billingservice.entity.InvoiceStatus;
import com.healthcare.billingservice.exception.UnauthorizedException;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {

    private Long id;

    private String invoiceNumber;

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    private Long appointmentId;

    private Long doctorId;

    @NotNull(message = "Invoice date is required")
    private LocalDate invoiceDate;

    @NotNull(message = "Due date is required")
    @Future(message = "Due date must be in the future")
    private LocalDate dueDate;

    private BigDecimal subtotal;

    @Min(value = 0, message = "Tax amount cannot be negative")
    private BigDecimal taxAmount;

    @Min(value = 0, message = "Discount amount cannot be negative")
    private BigDecimal discountAmount;

    private BigDecimal totalAmount;

    private BigDecimal paidAmount;

    private BigDecimal balanceDue;

    private InvoiceStatus status;

    @NotEmpty(message = "Invoice must have at least one item")
    private List<InvoiceItemDTO> items;

    private List<PaymentDTO> payments;

    private String patientName;

    @Email(message = "Patient email must be valid")
    private String patientEmail;

    private String patientPhone;

    private String insuranceProvider;

    private String insurancePolicyNumber;

    private BigDecimal insuranceClaimAmount;

    private String notes;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime sentAt;

    private LocalDateTime paidAt;

    private Long userId;

    public void checkUserId() {
        if (this.userId == null) {
            Long currentUserId = SecurityUtils.getCurrentUserId()
                    .orElseThrow(() -> new UnauthorizedException("User not authenticated"));
            this.userId = currentUserId;
        }
    }
}
