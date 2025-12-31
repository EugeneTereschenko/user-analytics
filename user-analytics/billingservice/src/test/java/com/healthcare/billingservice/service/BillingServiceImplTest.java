package com.healthcare.billingservice.service;

import com.healthcare.billingservice.dto.InvoiceDTO;
import com.healthcare.billingservice.dto.PaymentDTO;
import com.healthcare.billingservice.entity.*;
import com.healthcare.billingservice.exception.InvoiceNotFoundException;
import com.healthcare.billingservice.exception.InvalidPaymentException;
import com.healthcare.billingservice.mapper.InvoiceMapper;
import com.healthcare.billingservice.mapper.PaymentMapper;
import com.healthcare.billingservice.repository.InvoiceRepository;
import com.healthcare.billingservice.repository.PaymentRepository;
import com.healthcare.billingservice.testutil.InvoiceTestBuilder;
import com.healthcare.billingservice.testutil.PaymentTestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Billing Service Tests")
class BillingServiceImplTest {

    @Mock private InvoiceRepository invoiceRepository;
    @Mock private PaymentRepository paymentRepository;
    @Mock private InvoiceMapper invoiceMapper;
    @Mock private PaymentMapper paymentMapper;

    @InjectMocks
    private BillingServiceImpl billingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateInvoice() {
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setPatientId(1L);

        Invoice invoice = new InvoiceTestBuilder()
                .withPatientId(1L)
                .withInvoiceNumber("INV-1")
                .withInvoiceDate(LocalDate.now())
                .withTotalAmount(new BigDecimal("100.00"))
                .withStatus(InvoiceStatus.DRAFT)
                .build();

        when(invoiceMapper.toEntity(any(InvoiceDTO.class))).thenReturn(invoice);
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);
        when(invoiceMapper.toDTO(any(Invoice.class))).thenReturn(invoiceDTO);

        InvoiceDTO result = billingService.createInvoice(invoiceDTO);

        assertNotNull(result);
        verify(invoiceRepository).save(any(Invoice.class));
    }

    @Test
    void testGetInvoiceById_found() {
        Invoice invoice = new InvoiceTestBuilder().withId(1L).build();
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
        when(invoiceMapper.toDTO(invoice)).thenReturn(invoiceDTO);

        InvoiceDTO result = billingService.getInvoiceById(1L);

        assertNotNull(result);
        verify(invoiceRepository).findById(1L);
    }

    @Test
    void testGetInvoiceById_notFound() {
        when(invoiceRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(InvoiceNotFoundException.class, () -> billingService.getInvoiceById(1L));
    }

    @Test
    void testAddPayment_success() {
        Invoice invoice = new InvoiceTestBuilder()
                .withId(1L)
                .withTotalAmount(new BigDecimal("100.00"))
                .withPaidAmount(BigDecimal.ZERO)
                .withBalanceDue(new BigDecimal("100.00"))
                .withStatus(InvoiceStatus.SENT)
                .build();

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setAmount(new BigDecimal("50.00"));

        Payment payment = new PaymentTestBuilder()
                .withAmount(new BigDecimal("50.00"))
                .withPaymentDate(LocalDateTime.now())
                .build();

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));
        when(paymentMapper.toEntity(paymentDTO)).thenReturn(payment);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(paymentMapper.toDTO(payment)).thenReturn(paymentDTO);
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(invoice);

        PaymentDTO result = billingService.addPayment(1L, paymentDTO);

        assertNotNull(result);
        verify(paymentRepository).save(any(Payment.class));
        verify(invoiceRepository, atLeastOnce()).save(any(Invoice.class));
    }

    @Test
    void testAddPayment_exceedsBalance() {
        Invoice invoice = new InvoiceTestBuilder()
                .withId(1L)
                .withTotalAmount(new BigDecimal("100.00"))
                .withPaidAmount(BigDecimal.ZERO)
                .withBalanceDue(new BigDecimal("100.00"))
                .build();

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setAmount(new BigDecimal("150.00"));

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(invoice));

        assertThrows(InvalidPaymentException.class, () -> billingService.addPayment(1L, paymentDTO));
    }

    @Test
    void testDeleteInvoice_success() {
        when(invoiceRepository.existsById(1L)).thenReturn(true);
        doNothing().when(invoiceRepository).deleteById(1L);

        billingService.deleteInvoice(1L);

        verify(invoiceRepository).deleteById(1L);
    }

    @Test
    void testDeleteInvoice_notFound() {
        when(invoiceRepository.existsById(1L)).thenReturn(false);
        assertThrows(InvoiceNotFoundException.class, () -> billingService.deleteInvoice(1L));
    }

    // Add more tests for updateInvoice, sendInvoice, getAllInvoices, etc. as needed
}
