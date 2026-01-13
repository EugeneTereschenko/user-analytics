/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.billingservice.dto;

import com.example.common.security.util.SecurityUtils;
import com.healthcare.billingservice.entity.PaymentMethod;
import com.healthcare.billingservice.entity.PaymentStatus;
import com.healthcare.billingservice.exception.UnauthorizedException;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

    private Long id;

    private String paymentReference;

    @NotNull(message = "Payment date is required")
    private LocalDateTime paymentDate;

    @NotNull(message = "Amount is required")
    @Min(value = 0, message = "Amount cannot be negative")
    private BigDecimal amount;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    private PaymentStatus status;

    private String transactionId;

    private String cardLastFour;

    private String cardType;

    private String receiptNumber;

    private String notes;

    private String processedBy;

    private LocalDateTime createdAt;

    private Long userId;

    public void checkUserId() {
        if (this.userId == null) {
            Long currentUserId = SecurityUtils.getCurrentUserId()
                    .orElseThrow(() -> new UnauthorizedException("User not authenticated"));
            this.userId = currentUserId;
        }
    }
}
