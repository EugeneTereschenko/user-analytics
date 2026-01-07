package com.healthcare.billingservice.repository;

import com.example.common.security.client.AuthServiceClient;
import com.healthcare.billingservice.BillingServiceApplication;
import com.healthcare.billingservice.entity.*;
import com.healthcare.billingservice.testutil.InvoiceTestBuilder;
import com.healthcare.billingservice.testutil.PaymentTestBuilder;
import com.healthcare.billingservice.testutil.TestcontainersConfiguration;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {BillingServiceApplication.class})
@Import({TestcontainersConfiguration.class,  FeignAutoConfiguration.class})
class PaymentRepositoryTest {

    @Autowired
    private PaymentRepository paymentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @MockBean
    private AuthServiceClient authServiceClient;

    private Invoice persistInvoice(Long patientId) {
        Invoice invoice = new InvoiceTestBuilder()
                .withPatientId(patientId)
                .withInvoiceNumber("INV-" + patientId)
                .withInvoiceDate(LocalDate.now())
                .withTotalAmount(new BigDecimal("200.00"))
                .withStatus(InvoiceStatus.PAID)
                .build();
        invoice = entityManager.merge(invoice); // Use merge instead of persist
        entityManager.flush();
        return invoice;
    }



    @Test
    @WithMockUser
    void testFindByPaymentReference() {
        Invoice invoice = persistInvoice(1L);
        Payment payment = new PaymentTestBuilder()
                .withInvoice(invoice)
                .withPaymentReference("REF-001")
                .build();
        paymentRepository.save(payment);

        Optional<Payment> found = paymentRepository.findByPaymentReference("REF-001");
        assertTrue(found.isPresent());
        assertEquals("REF-001", found.get().getPaymentReference());
    }

    @Test
    @WithMockUser
    void testFindByInvoiceId() {
        Invoice invoice = persistInvoice(2L);
        Payment payment = new PaymentTestBuilder()
                .withInvoice(invoice)
                .build();
        paymentRepository.save(payment);

        List<Payment> payments = paymentRepository.findByInvoiceId(invoice.getId());
        assertFalse(payments.isEmpty());
        assertEquals(invoice.getId(), payments.get(0).getInvoice().getId());
    }

    @Test
    @WithMockUser
    void testFindByPatientId() {
        Invoice invoice = persistInvoice(3L);
        Payment payment = new PaymentTestBuilder()
                .withInvoice(invoice)
                .withPaymentDate(LocalDateTime.now())
                .build();
        paymentRepository.save(payment);

        List<Payment> payments = paymentRepository.findByPatientId(3L);
        assertFalse(payments.isEmpty());
        assertEquals(3L, payments.get(0).getInvoice().getPatientId());
    }

    @Test
    @WithMockUser
    void testGetTotalPaymentsBetween() {
        Invoice invoice = persistInvoice(4L);
        Payment payment = new PaymentTestBuilder()
                .withInvoice(invoice)
                .withAmount(new BigDecimal("150.00"))
                .withStatus(PaymentStatus.COMPLETED)
                .withPaymentDate(LocalDateTime.now())
                .build();
        paymentRepository.save(payment);

        BigDecimal total = paymentRepository.getTotalPaymentsBetween(
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1)
        );
        assertNotNull(total);
        assertEquals(new BigDecimal("150.00"), total);
    }

    @Test
    @WithMockUser
    void testFindPaymentsByMethodBetween() {
        Invoice invoice = persistInvoice(5L);
        Payment payment = new PaymentTestBuilder()
                .withInvoice(invoice)
                .withPaymentMethod(PaymentMethod.CREDIT_CARD)
                .withPaymentDate(LocalDateTime.now())
                .build();
        paymentRepository.save(payment);

        List<Payment> payments = paymentRepository.findPaymentsByMethodBetween(
                PaymentMethod.CREDIT_CARD,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1)
        );
        assertFalse(payments.isEmpty());
        assertEquals(PaymentMethod.CREDIT_CARD, payments.get(0).getPaymentMethod());
    }
}
