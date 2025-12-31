package com.healthcare.billingservice.controller;

import com.healthcare.billingservice.dto.InvoiceDTO;
import com.healthcare.billingservice.dto.PaymentDTO;
import com.healthcare.billingservice.entity.Invoice;
import com.healthcare.billingservice.entity.InvoiceStatus;
import com.healthcare.billingservice.service.impl.BillingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/billing")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    @PostMapping("/invoices")
    public ResponseEntity<InvoiceDTO> createInvoice(@Valid @RequestBody InvoiceDTO invoiceDTO) {
        InvoiceDTO created = billingService.createInvoice(invoiceDTO);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/invoices/{id}")
    public ResponseEntity<InvoiceDTO> getInvoiceById(@PathVariable Long id) {
        InvoiceDTO invoice = billingService.getInvoiceById(id);
        return ResponseEntity.ok(invoice);
    }

    @GetMapping("/invoices/number/{invoiceNumber}")
    public ResponseEntity<InvoiceDTO> getInvoiceByNumber(@PathVariable String invoiceNumber) {
        InvoiceDTO invoice = billingService.getInvoiceByNumber(invoiceNumber);
        return ResponseEntity.ok(invoice);
    }

    @GetMapping("/invoices")
    public ResponseEntity<Page<InvoiceDTO>> getAllInvoices(Pageable pageable) {
        Page<InvoiceDTO> invoices = billingService.getAllInvoices(pageable);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/invoices/patient/{patientId}")
    public ResponseEntity<Page<InvoiceDTO>> getInvoicesByPatient(
            @PathVariable Long patientId,
            Pageable pageable) {
        Page<InvoiceDTO> invoices = billingService.getInvoicesByPatient(patientId, pageable);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/invoices/status/{status}")
    public ResponseEntity<Page<InvoiceDTO>> getInvoicesByStatus(
            @PathVariable InvoiceStatus status,
            Pageable pageable) {
        Page<InvoiceDTO> invoices = billingService.getInvoicesByStatus(status, pageable);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/invoices/overdue")
    public ResponseEntity<List<InvoiceDTO>> getOverdueInvoices() {
        List<InvoiceDTO> invoices = billingService.getOverdueInvoices();
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/invoices/patient/{patientId}/outstanding")
    public ResponseEntity<List<InvoiceDTO>> getPatientOutstandingInvoices(
            @PathVariable Long patientId) {
        List<InvoiceDTO> invoices = billingService.getPatientOutstandingInvoices(patientId);
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/invoices/outstanding/total")
    public ResponseEntity<BigDecimal> getTotalOutstanding() {
        BigDecimal total = billingService.getTotalOutstanding();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/invoices/search")
    public ResponseEntity<Page<InvoiceDTO>> searchInvoices(
            @RequestParam String query,
            Pageable pageable) {
        Page<InvoiceDTO> invoices = billingService.searchInvoices(query, pageable);
        return ResponseEntity.ok(invoices);
    }

    @PutMapping("/invoices/{id}")
    public ResponseEntity<InvoiceDTO> updateInvoice(
            @PathVariable Long id,
            @Valid @RequestBody InvoiceDTO invoiceDTO) {
        InvoiceDTO updated = billingService.updateInvoice(id, invoiceDTO);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/invoices/{id}/send")
    public ResponseEntity<InvoiceDTO> sendInvoice(@PathVariable Long id) {
        InvoiceDTO sent = billingService.sendInvoice(id);
        return ResponseEntity.ok(sent);
    }

    @PostMapping("/invoices/{invoiceId}/payments")
    public ResponseEntity<PaymentDTO> addPayment(
            @PathVariable Long invoiceId,
            @Valid @RequestBody PaymentDTO paymentDTO) {
        PaymentDTO payment = billingService.addPayment(invoiceId, paymentDTO);
        return new ResponseEntity<>(payment, HttpStatus.CREATED);
    }

    @PatchMapping("/invoices/{id}/cancel")
    public ResponseEntity<InvoiceDTO> cancelInvoice(@PathVariable Long id) {
        InvoiceDTO cancelled = billingService.cancelInvoice(id);
        return ResponseEntity.ok(cancelled);
    }

    @DeleteMapping("/invoices/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        billingService.deleteInvoice(id);
        return ResponseEntity.noContent().build();
    }
}