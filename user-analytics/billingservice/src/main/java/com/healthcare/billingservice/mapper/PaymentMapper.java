/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.billingservice.mapper;


import com.healthcare.billingservice.dto.PaymentDTO;
import com.healthcare.billingservice.entity.Payment;
import com.healthcare.billingservice.entity.PaymentStatus;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentDTO toDTO(Payment payment) {
        if (payment == null) return null;

        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());
        dto.setPaymentReference(payment.getPaymentReference());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setStatus(payment.getStatus());
        dto.setTransactionId(payment.getTransactionId());
        dto.setCardLastFour(payment.getCardLastFour());
        dto.setCardType(payment.getCardType());
        dto.setReceiptNumber(payment.getReceiptNumber());
        dto.setNotes(payment.getNotes());
        dto.setProcessedBy(payment.getProcessedBy());
        dto.setCreatedAt(payment.getCreatedAt());
        return dto;
    }

    public Payment toEntity(PaymentDTO dto) {
        if (dto == null) return null;

        Payment payment = new Payment();
        payment.setId(dto.getId());
        payment.setPaymentReference(dto.getPaymentReference());
        payment.setPaymentDate(dto.getPaymentDate());
        payment.setAmount(dto.getAmount());
        payment.setPaymentMethod(dto.getPaymentMethod());
        payment.setStatus(dto.getStatus() != null ? dto.getStatus() : PaymentStatus.COMPLETED);
        payment.setTransactionId(dto.getTransactionId());
        payment.setCardLastFour(dto.getCardLastFour());
        payment.setCardType(dto.getCardType());
        payment.setReceiptNumber(dto.getReceiptNumber());
        payment.setNotes(dto.getNotes());
        payment.setProcessedBy(dto.getProcessedBy());
        return payment;
    }
}
