/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */


package com.healthcare.billingservice.service;

import com.healthcare.billingservice.dto.InvoiceDTO;
import com.healthcare.billingservice.dto.PaymentDTO;
import com.healthcare.billingservice.entity.Invoice;
import com.healthcare.billingservice.entity.InvoiceItem;
import com.healthcare.billingservice.entity.InvoiceStatus;
import com.healthcare.billingservice.entity.Payment;
import com.healthcare.billingservice.exception.InvoiceNotFoundException;
import com.healthcare.billingservice.exception.InvalidPaymentException;
import com.healthcare.billingservice.mapper.InvoiceMapper;
import com.healthcare.billingservice.mapper.PaymentMapper;
import com.healthcare.billingservice.repository.InvoiceRepository;
import com.healthcare.billingservice.repository.PaymentRepository;
import com.healthcare.billingservice.service.impl.BillingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class BillingServiceImpl implements BillingService {
    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final InvoiceMapper invoiceMapper;
    private final PaymentMapper paymentMapper;

    @Override
    public InvoiceDTO createInvoice(InvoiceDTO invoiceDTO) {
        log.info("Creating invoice for patient: {}", invoiceDTO.getPatientId());

        String invoiceNumber = generateInvoiceNumber();
        invoiceDTO.setInvoiceNumber(invoiceNumber);

        // Use the mapper method that handles items correctly
        Invoice invoice = invoiceMapper.toEntitywithItems(invoiceDTO);

        // Calculate totals for each item
        if (invoice.getItems() != null) {
            invoice.getItems().forEach(InvoiceItem::calculateTotalPrice);
        }

        invoice.calculateTotals();
        Invoice savedInvoice = invoiceRepository.save(invoice);

        log.info("Invoice created successfully with number: {}", invoiceNumber);
        return invoiceMapper.toDTO(savedInvoice);
    }

    @Transactional(readOnly = true)
    @Override
    public InvoiceDTO getInvoiceById(Long id) {
        log.info("Fetching invoice with ID: {}", id);

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException(
                        "Invoice not found with ID: " + id));

        return invoiceMapper.toDTO(invoice);
    }

    @Transactional(readOnly = true)
    @Override
    public InvoiceDTO getInvoiceByNumber(String invoiceNumber) {
        log.info("Fetching invoice with number: {}", invoiceNumber);

        Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new InvoiceNotFoundException(
                        "Invoice not found with number: " + invoiceNumber));

        return invoiceMapper.toDTO(invoice);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<InvoiceDTO> getAllInvoices(Pageable pageable) {
        log.info("Fetching all invoices with pagination");
        return invoiceRepository.findAll(pageable)
                .map(invoiceMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<InvoiceDTO> getInvoicesByPatient(Long patientId, Pageable pageable) {
        log.info("Fetching invoices for patient: {}", patientId);
        return invoiceRepository.findByPatientId(patientId, pageable)
                .map(invoiceMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<InvoiceDTO> getInvoicesByStatus(InvoiceStatus status, Pageable pageable) {
        log.info("Fetching invoices with status: {}", status);
        return invoiceRepository.findByStatus(status, pageable)
                .map(invoiceMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public List<InvoiceDTO> getOverdueInvoices() {
        log.info("Fetching overdue invoices");
        return invoiceRepository.findOverdueInvoices(InvoiceStatus.SENT, LocalDate.now())
                .stream()
                .map(invoiceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<InvoiceDTO> getPatientOutstandingInvoices(Long patientId) {
        log.info("Fetching outstanding invoices for patient: {}", patientId);
        return invoiceRepository.findPatientOutstandingInvoices(patientId)
                .stream()
                .map(invoiceMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public BigDecimal getTotalOutstanding() {
        log.info("Calculating total outstanding amount");
        BigDecimal total = invoiceRepository.getTotalOutstanding();
        return total != null ? total : BigDecimal.ZERO;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<InvoiceDTO> searchInvoices(String searchTerm, Pageable pageable) {
        log.info("Searching invoices with term: {}", searchTerm);
        return invoiceRepository.searchInvoices(searchTerm, pageable)
                .map(invoiceMapper::toDTO);
    }

    @Override
    public InvoiceDTO updateInvoice(Long id, InvoiceDTO invoiceDTO) {
        log.info("Updating invoice with ID: {}", id);

        Invoice existingInvoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException(
                        "Invoice not found with ID: " + id));

        invoiceMapper.updateEntityFromDTO(invoiceDTO, existingInvoice);

        if (existingInvoice.getItems() != null) {
            existingInvoice.getItems().forEach(InvoiceItem::calculateTotalPrice);
        }

        existingInvoice.calculateTotals();
        Invoice updatedInvoice = invoiceRepository.save(existingInvoice);

        log.info("Invoice updated successfully");
        return invoiceMapper.toDTO(updatedInvoice);
    }

    @Override
    public InvoiceDTO sendInvoice(Long id) {
        log.info("Sending invoice with ID: {}", id);

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException(
                        "Invoice not found with ID: " + id));

        invoice.setStatus(InvoiceStatus.SENT);
        invoice.setSentAt(LocalDateTime.now());
        Invoice sentInvoice = invoiceRepository.save(invoice);

        log.info("Invoice sent successfully");
        return invoiceMapper.toDTO(sentInvoice);
    }

    @Override
    public PaymentDTO addPayment(Long invoiceId, PaymentDTO paymentDTO) {
        log.info("Adding payment to invoice: {}", invoiceId);

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new InvoiceNotFoundException(
                        "Invoice not found with ID: " + invoiceId));

        if (paymentDTO.getAmount().compareTo(invoice.getBalanceDue()) > 0) {
            throw new InvalidPaymentException(
                    "Payment amount exceeds balance due");
        }

        Payment payment = paymentMapper.toEntity(paymentDTO);
        payment.setInvoice(invoice);
        payment.setPaymentReference(generatePaymentReference());

        Payment savedPayment = paymentRepository.save(payment);

        // Update invoice amounts
        invoice.setPaidAmount(invoice.getPaidAmount().add(payment.getAmount()));
        invoice.setBalanceDue(invoice.getTotalAmount().subtract(invoice.getPaidAmount()));

        // Update invoice status
        if (invoice.getBalanceDue().compareTo(BigDecimal.ZERO) == 0) {
            invoice.setStatus(InvoiceStatus.PAID);
            invoice.setPaidAt(LocalDateTime.now());
        } else if (invoice.getPaidAmount().compareTo(BigDecimal.ZERO) > 0) {
            invoice.setStatus(InvoiceStatus.PARTIALLY_PAID);
        }

        invoiceRepository.save(invoice);

        log.info("Payment added successfully");
        return paymentMapper.toDTO(savedPayment);
    }

    @Override
    public InvoiceDTO cancelInvoice(Long id) {
        log.info("Cancelling invoice with ID: {}", id);

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException(
                        "Invoice not found with ID: " + id));

        invoice.setStatus(InvoiceStatus.CANCELLED);
        Invoice cancelledInvoice = invoiceRepository.save(invoice);

        log.info("Invoice cancelled successfully");
        return invoiceMapper.toDTO(cancelledInvoice);
    }

    @Override
    public void deleteInvoice(Long id) {
        log.info("Deleting invoice with ID: {}", id);

        if (!invoiceRepository.existsById(id)) {
            throw new InvoiceNotFoundException("Invoice not found with ID: " + id);
        }

        invoiceRepository.deleteById(id);
        log.info("Invoice deleted successfully");
    }

    private String generateInvoiceNumber() {
        String prefix = "INV";
        String timestamp = String.valueOf(System.currentTimeMillis());
        return prefix + "-" + timestamp.substring(timestamp.length() - 10);
    }

    private String generatePaymentReference() {
        return "PAY-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
