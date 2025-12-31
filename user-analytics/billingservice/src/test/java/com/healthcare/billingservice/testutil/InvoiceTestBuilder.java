package com.healthcare.billingservice.testutil;

import com.healthcare.billingservice.dto.InvoiceDTO;
import com.healthcare.billingservice.dto.InvoiceItemDTO;
import com.healthcare.billingservice.entity.Invoice;
import com.healthcare.billingservice.entity.InvoiceStatus;
import com.healthcare.billingservice.entity.Payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class InvoiceTestBuilder {
    private Long id = 1L;
    private String invoiceNumber = "INV-001";
    private Long patientId = 1L;
    private Long appointmentId = 1L;
    private Long doctorId = 1L;
    private LocalDate invoiceDate = LocalDate.now();
    private LocalDate dueDate = LocalDate.now().plusDays(30);
    private BigDecimal subtotal = BigDecimal.valueOf(100);
    private BigDecimal taxAmount = BigDecimal.valueOf(10);
    private BigDecimal discountAmount = BigDecimal.valueOf(5);
    private BigDecimal totalAmount = BigDecimal.valueOf(105);
    private BigDecimal paidAmount = BigDecimal.ZERO;
    private BigDecimal balanceDue = BigDecimal.valueOf(105);
    private InvoiceStatus status = InvoiceStatus.PENDING;
    private List<InvoiceItemDTO> items = new ArrayList<>();
    private List<Payment> payments = new ArrayList<>();
    private String patientName = "John Doe";
    private String patientEmail = "john@example.com";
    private String patientPhone = "1234567890";
    private String insuranceProvider = "Provider";
    private String insurancePolicyNumber = "POL123";
    private BigDecimal insuranceClaimAmount = BigDecimal.ZERO;
    private String notes = "";
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
    private LocalDateTime sentAt = null;
    private LocalDateTime paidAt = null;

    public InvoiceTestBuilder withId(Long id) { this.id = id; return this; }
    public InvoiceTestBuilder withInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; return this; }
    public InvoiceTestBuilder withPatientId(Long patientId) { this.patientId = patientId; return this; }
    public InvoiceTestBuilder withAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; return this; }
    public InvoiceTestBuilder withDoctorId(Long doctorId) { this.doctorId = doctorId; return this; }
    public InvoiceTestBuilder withInvoiceDate(LocalDate invoiceDate) { this.invoiceDate = invoiceDate; return this; }
    public InvoiceTestBuilder withDueDate(LocalDate dueDate) { this.dueDate = dueDate; return this; }
    public InvoiceTestBuilder withSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; return this; }
    public InvoiceTestBuilder withTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; return this; }
    public InvoiceTestBuilder withDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; return this; }
    public InvoiceTestBuilder withTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }
    public InvoiceTestBuilder withPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; return this; }
    public InvoiceTestBuilder withBalanceDue(BigDecimal balanceDue) { this.balanceDue = balanceDue; return this; }
    public InvoiceTestBuilder withStatus(InvoiceStatus status) { this.status = status; return this; }
    public InvoiceTestBuilder withItems(List<InvoiceItemDTO> items) { this.items = items; return this; }
    public InvoiceTestBuilder withPayments(List<Payment> payments) { this.payments = payments; return this; }
    public InvoiceTestBuilder withPatientName(String patientName) { this.patientName = patientName; return this; }
    public InvoiceTestBuilder withPatientEmail(String patientEmail) { this.patientEmail = patientEmail; return this; }
    public InvoiceTestBuilder withPatientPhone(String patientPhone) { this.patientPhone = patientPhone; return this; }
    public InvoiceTestBuilder withInsuranceProvider(String insuranceProvider) { this.insuranceProvider = insuranceProvider; return this; }
    public InvoiceTestBuilder withInsurancePolicyNumber(String insurancePolicyNumber) { this.insurancePolicyNumber = insurancePolicyNumber; return this; }
    public InvoiceTestBuilder withInsuranceClaimAmount(BigDecimal insuranceClaimAmount) { this.insuranceClaimAmount = insuranceClaimAmount; return this; }
    public InvoiceTestBuilder withNotes(String notes) { this.notes = notes; return this; }
    public InvoiceTestBuilder withCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
    public InvoiceTestBuilder withUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
    public InvoiceTestBuilder withSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; return this; }
    public InvoiceTestBuilder withPaidAt(LocalDateTime paidAt) { this.paidAt = paidAt; return this; }

    public Invoice build() {
        // This method still uses entities for domain model
        return new Invoice(
                id, invoiceNumber, patientId, appointmentId, doctorId, invoiceDate, dueDate,
                subtotal, taxAmount, discountAmount, totalAmount, paidAmount, balanceDue, status,
                new ArrayList<>(), // items as entities if needed
                payments, patientName, patientEmail, patientPhone, insuranceProvider,
                insurancePolicyNumber, insuranceClaimAmount, notes, createdAt, updatedAt, sentAt, paidAt
        );
    }

    public static InvoiceTestBuilder anInvoice() {
        return new InvoiceTestBuilder();
    }

    public InvoiceDTO buildDTO() {
        InvoiceDTO dto = new InvoiceDTO();
        dto.setId(id);
        dto.setInvoiceNumber(invoiceNumber);
        dto.setPatientId(patientId);
        dto.setAppointmentId(appointmentId);
        dto.setDoctorId(doctorId);
        dto.setInvoiceDate(invoiceDate);
        dto.setDueDate(dueDate);
        dto.setSubtotal(subtotal);
        dto.setTaxAmount(taxAmount);
        dto.setDiscountAmount(discountAmount);
        dto.setTotalAmount(totalAmount);
        dto.setPaidAmount(paidAmount);
        dto.setBalanceDue(balanceDue);
        dto.setStatus(status);
        dto.setItems(items); // Now uses DTOs
        dto.setPayments(new ArrayList<>()); // If PaymentDTO is needed, map accordingly
        dto.setPatientName(patientName);
        dto.setPatientEmail(patientEmail);
        dto.setPatientPhone(patientPhone);
        dto.setInsuranceProvider(insuranceProvider);
        dto.setInsurancePolicyNumber(insurancePolicyNumber);
        dto.setInsuranceClaimAmount(insuranceClaimAmount);
        dto.setNotes(notes);
        dto.setCreatedAt(createdAt);
        dto.setUpdatedAt(updatedAt);
        dto.setSentAt(sentAt);
        dto.setPaidAt(paidAt);
        return dto;
    }
}
