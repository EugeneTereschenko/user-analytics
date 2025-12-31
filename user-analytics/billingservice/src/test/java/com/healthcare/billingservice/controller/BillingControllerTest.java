package com.healthcare.billingservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.billingservice.BillingServiceApplication;
import com.healthcare.billingservice.dto.InvoiceDTO;
import com.healthcare.billingservice.dto.InvoiceItemDTO;
import com.healthcare.billingservice.dto.PaymentDTO;
import com.healthcare.billingservice.entity.InvoiceStatus;
import com.healthcare.billingservice.testutil.InvoiceTestBuilder;
import com.healthcare.billingservice.testutil.PaymentTestBuilder;
import com.healthcare.billingservice.testutil.TestcontainersConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {BillingServiceApplication.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
@Transactional
class BillingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static InvoiceDTO getInvoiceDTO() {

        InvoiceItemDTO itemDTO = new InvoiceItemDTO();
        itemDTO.setDescription("Consultation");
        itemDTO.setQuantity(1);
        itemDTO.setUnitPrice(new BigDecimal("100.00"));


        InvoiceDTO invoiceDTO = new InvoiceTestBuilder()
                .withPatientId(1L)
                .withInvoiceNumber("INV-1")
                .withInvoiceDate(LocalDate.now())
                .withTotalAmount(new BigDecimal("100.00"))
                .withStatus(InvoiceStatus.DRAFT)
                .withItems(List.of(itemDTO)) // Ensure this is List<InvoiceItemDTO>
                .buildDTO();


        return invoiceDTO;
    }

/*    @Test
    @DisplayName("POST /api/v1/billing/invoices - create invoice")
    void testCreateInvoice() throws Exception {
        InvoiceDTO invoiceDTO = getInvoiceDTO();


        mockMvc.perform(post("/api/v1/billing/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invoiceDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.patientId", is(1)));
    }

    @Test
    @DisplayName("GET /api/v1/billing/invoices/{id} - get invoice by id")
    void testGetInvoiceById() throws Exception {
        InvoiceDTO invoiceDTO = getInvoiceDTO();

        String response = mockMvc.perform(post("/api/v1/billing/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invoiceDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        InvoiceDTO created = objectMapper.readValue(response, InvoiceDTO.class);

        mockMvc.perform(get("/api/v1/billing/invoices/{id}", created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.invoiceNumber", is("INV-2")));
    }

    @Test
    @DisplayName("POST /api/v1/billing/invoices/{invoiceId}/payments - add payment")
    void testAddPayment() throws Exception {
        InvoiceDTO invoiceDTO = getInvoiceDTO();

        String invoiceResponse = mockMvc.perform(post("/api/v1/billing/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invoiceDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        InvoiceDTO createdInvoice = objectMapper.readValue(invoiceResponse, InvoiceDTO.class);

        PaymentDTO paymentDTO = new PaymentTestBuilder()
                .withAmount(new BigDecimal("100.00"))
                .withPaymentDate(LocalDateTime.now())
                .buildDTO();

        mockMvc.perform(post("/api/v1/billing/invoices/{invoiceId}/payments", createdInvoice.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount", is(100.00)));
    }

    @Test
    @DisplayName("DELETE /api/v1/billing/invoices/{id} - delete invoice")
    void testDeleteInvoice() throws Exception {
        InvoiceDTO invoiceDTO = getInvoiceDTO();

        String response = mockMvc.perform(post("/api/v1/billing/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invoiceDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        InvoiceDTO created = objectMapper.readValue(response, InvoiceDTO.class);

        mockMvc.perform(delete("/api/v1/billing/invoices/{id}", created.getId()))
                .andExpect(status().isNoContent());
    }*/
}
