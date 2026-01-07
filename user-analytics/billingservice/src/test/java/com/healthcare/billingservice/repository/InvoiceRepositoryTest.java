package com.healthcare.billingservice.repository;

import com.example.common.security.client.AuthServiceClient;
import com.healthcare.billingservice.BillingServiceApplication;
import com.healthcare.billingservice.entity.Invoice;
import com.healthcare.billingservice.entity.InvoiceStatus;
import com.healthcare.billingservice.testutil.InvoiceTestBuilder;
import com.healthcare.billingservice.testutil.TestcontainersConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {BillingServiceApplication.class})
@Import({TestcontainersConfiguration.class,  FeignAutoConfiguration.class})
class InvoiceRepositoryTest {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @MockBean
    private AuthServiceClient authServiceClient;

    private Invoice invoice1;
    private Invoice invoice2;

    @BeforeEach
    void setUp() {
        invoice1 = InvoiceTestBuilder.anInvoice()
                .withInvoiceNumber("INV-001")
                .withPatientId(1L)
                .withDoctorId(10L)
                .withStatus(InvoiceStatus.PAID)
                .withInvoiceDate(LocalDate.now().minusDays(10))
                .withDueDate(LocalDate.now().minusDays(5))
                .withTotalAmount(new BigDecimal("100.00"))
                .withBalanceDue(BigDecimal.ZERO)
                .build();

        invoice2 = InvoiceTestBuilder.anInvoice()
                .withInvoiceNumber("INV-002")
                .withPatientId(1L)
                .withDoctorId(10L)
                .withStatus(InvoiceStatus.OVERDUE)
                .withInvoiceDate(LocalDate.now().minusDays(20))
                .withDueDate(LocalDate.now().minusDays(1))
                .withTotalAmount(new BigDecimal("200.00"))
                .withBalanceDue(new BigDecimal("200.00"))
                .build();

        invoiceRepository.save(invoice1);
        invoiceRepository.save(invoice2);
    }

    @Test
    @WithMockUser
    void testFindByInvoiceNumber() {
        Optional<Invoice> found = invoiceRepository.findByInvoiceNumber("INV-001");
        assertTrue(found.isPresent());
        assertEquals(invoice1.getInvoiceNumber(), found.get().getInvoiceNumber());
    }

    @Test
    @WithMockUser
    void testExistsByInvoiceNumber() {
        assertTrue(invoiceRepository.existsByInvoiceNumber("INV-001"));
        assertFalse(invoiceRepository.existsByInvoiceNumber("INV-999"));
    }

    @Test
    @WithMockUser
    void testFindByPatientId() {
        var page = invoiceRepository.findByPatientId(1L, PageRequest.of(0, 10));
        assertEquals(2, page.getTotalElements());
    }

    @Test
    @WithMockUser
    void testFindOverdueInvoices() {
        List<Invoice> overdue = invoiceRepository.findOverdueInvoices(InvoiceStatus.OVERDUE, LocalDate.now());
        assertFalse(overdue.isEmpty());
        assertEquals(InvoiceStatus.OVERDUE, overdue.get(0).getStatus());
    }

    @Test
    @WithMockUser
    void testGetTotalRevenueBetween() {
        BigDecimal revenue = invoiceRepository.getTotalRevenueBetween(LocalDate.now().minusDays(30), LocalDate.now());
        assertEquals(new BigDecimal("100.00"), revenue);
    }

    @Test
    @WithMockUser
    void testGetTotalOutstanding() {
        BigDecimal outstanding = invoiceRepository.getTotalOutstanding();
        assertEquals(new BigDecimal("200.00"), outstanding);
    }

    @Test
    @WithMockUser
    void testCountOverdueInvoices() {
        Long count = invoiceRepository.countOverdueInvoices();
        assertEquals(1L, count);
    }

    @Test
    @WithMockUser
    void testFindPatientOutstandingInvoices() {
        List<Invoice> outstanding = invoiceRepository.findPatientOutstandingInvoices(1L);
        assertEquals(1, outstanding.size());
        assertEquals("INV-002", outstanding.get(0).getInvoiceNumber());
    }
}
