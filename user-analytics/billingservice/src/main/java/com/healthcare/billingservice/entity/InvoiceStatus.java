/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.billingservice.entity;

public enum InvoiceStatus {
    DRAFT,
    PENDING,
    SENT,
    PARTIALLY_PAID,
    PAID,
    OVERDUE,
    CANCELLED,
    REFUNDED
}
