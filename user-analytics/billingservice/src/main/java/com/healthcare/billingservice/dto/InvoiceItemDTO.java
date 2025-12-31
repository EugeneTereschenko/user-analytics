/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.billingservice.dto;

import com.healthcare.billingservice.entity.InvoiceItem;
import com.healthcare.billingservice.entity.ItemType;
import com.healthcare.billingservice.entity.Payment;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
class InvoiceItemDTO {

    private Long id;

    @NotNull(message = "Item type is required")
    private ItemType itemType;

    @NotBlank(message = "Description is required")
    @Size(max = 200, message = "Description must not exceed 200 characters")
    private String description;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Unit price is required")
    @Min(value = 0, message = "Unit price cannot be negative")
    private BigDecimal unitPrice;

    private BigDecimal totalPrice;

    private String serviceCode;

    private Long medicalRecordId;
}
