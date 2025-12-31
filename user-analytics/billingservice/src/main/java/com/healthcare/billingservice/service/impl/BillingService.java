/*
 * © 2025 Yevhen Tereshchenko
 * All rights reserved.
 *
 */

package com.healthcare.billingservice.service.impl;

import com.healthcare.billingservice.dto.InvoiceDTO;
import com.healthcare.billingservice.dto.PaymentDTO;
import com.healthcare.billingservice.entity.InvoiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface BillingService {
    InvoiceDTO createInvoice(InvoiceDTO invoiceDTO);

    @Transactional(readOnly = true)
    InvoiceDTO getInvoiceById(Long id);

    @Transactional(readOnly = true)
    InvoiceDTO getInvoiceByNumber(String invoiceNumber);

    @Transactional(readOnly = true)
    Page<InvoiceDTO> getAllInvoices(Pageable pageable);

    @Transactional(readOnly = true)
    Page<InvoiceDTO> getInvoicesByPatient(Long patientId, Pageable pageable);

    @Transactional(readOnly = true)
    Page<InvoiceDTO> getInvoicesByStatus(InvoiceStatus status, Pageable pageable);

    @Transactional(readOnly = true)
    List<InvoiceDTO> getOverdueInvoices();

    @Transactional(readOnly = true)
    List<InvoiceDTO> getPatientOutstandingInvoices(Long patientId);

    @Transactional(readOnly = true)
    BigDecimal getTotalOutstanding();

    @Transactional(readOnly = true)
    Page<InvoiceDTO> searchInvoices(String searchTerm, Pageable pageable);

    InvoiceDTO updateInvoice(Long id, InvoiceDTO invoiceDTO);

    InvoiceDTO sendInvoice(Long id);

    PaymentDTO addPayment(Long invoiceId, PaymentDTO paymentDTO);

    InvoiceDTO cancelInvoice(Long id);

    void deleteInvoice(Long id);
}
