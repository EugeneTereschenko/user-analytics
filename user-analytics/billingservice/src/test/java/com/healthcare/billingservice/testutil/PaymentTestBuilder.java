package com.healthcare.billingservice.testutil;

import com.healthcare.billingservice.dto.PaymentDTO;
import com.healthcare.billingservice.entity.Invoice;
import com.healthcare.billingservice.entity.Payment;
import com.healthcare.billingservice.entity.PaymentMethod;
import com.healthcare.billingservice.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentTestBuilder {
    private Long id = 1L;
    private Invoice invoice = new Invoice(); // You may want to use an InvoiceTestBuilder
    private String paymentReference = "REF123";
    private LocalDateTime paymentDate = LocalDateTime.now();
    private BigDecimal amount = new BigDecimal("100.00");
    private PaymentMethod paymentMethod = PaymentMethod.CREDIT_CARD;
    private PaymentStatus status = PaymentStatus.COMPLETED;
    private String transactionId = "TXN123";
    private String cardLastFour = "1234";
    private String cardType = "VISA";
    private String receiptNumber = "RCPT123";
    private String notes = "Test payment";
    private String processedBy = "admin";
    private LocalDateTime createdAt = LocalDateTime.now();

    public PaymentTestBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public PaymentTestBuilder withInvoice(Invoice invoice) {
        this.invoice = invoice;
        return this;
    }

    public PaymentTestBuilder withPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
        return this;
    }

    public PaymentTestBuilder withPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
        return this;
    }

    public PaymentTestBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public PaymentTestBuilder withPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public PaymentTestBuilder withStatus(PaymentStatus status) {
        this.status = status;
        return this;
    }

    public PaymentTestBuilder withTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public PaymentTestBuilder withCardLastFour(String cardLastFour) {
        this.cardLastFour = cardLastFour;
        return this;
    }

    public PaymentTestBuilder withCardType(String cardType) {
        this.cardType = cardType;
        return this;
    }

    public PaymentTestBuilder withReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
        return this;
    }

    public PaymentTestBuilder withNotes(String notes) {
        this.notes = notes;
        return this;
    }

    public PaymentTestBuilder withProcessedBy(String processedBy) {
        this.processedBy = processedBy;
        return this;
    }

    public PaymentTestBuilder withCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Payment build() {
        return new Payment(
                id,
                invoice,
                paymentReference,
                paymentDate,
                amount,
                paymentMethod,
                status,
                transactionId,
                cardLastFour,
                cardType,
                receiptNumber,
                notes,
                processedBy,
                createdAt
        );
    }

    public PaymentDTO buildDTO() {
        PaymentDTO dto = new PaymentDTO();
        dto.setId(id);
        dto.setPaymentReference(paymentReference);
        dto.setPaymentDate(paymentDate);
        dto.setAmount(amount);
        dto.setPaymentMethod(paymentMethod);
        dto.setStatus(status);
        dto.setTransactionId(transactionId);
        dto.setCardLastFour(cardLastFour);
        dto.setCardType(cardType);
        dto.setReceiptNumber(receiptNumber);
        dto.setNotes(notes);
        dto.setProcessedBy(processedBy);
        dto.setCreatedAt(createdAt);
        return dto;
    }

}

