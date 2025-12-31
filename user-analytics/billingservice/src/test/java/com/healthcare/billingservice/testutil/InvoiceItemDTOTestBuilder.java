package com.healthcare.billingservice.testutil;

import com.healthcare.billingservice.dto.InvoiceItemDTO;
import com.healthcare.billingservice.entity.ItemType;

import java.math.BigDecimal;

public class InvoiceItemDTOTestBuilder {
    private Long id = 1L;
    private String description = "Consultation";
    private Integer quantity = 1;
    private BigDecimal unitPrice = new BigDecimal("100.00");
    private BigDecimal total = new BigDecimal("100.00");

    public InvoiceItemDTOTestBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public InvoiceItemDTOTestBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public InvoiceItemDTOTestBuilder withQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public InvoiceItemDTOTestBuilder withUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
        return this;
    }

    public InvoiceItemDTOTestBuilder withTotal(BigDecimal total) {
        this.total = total;
        return this;
    }

    public static InvoiceItemDTOTestBuilder anInvoiceItem() {
        return new InvoiceItemDTOTestBuilder();
    }

    public InvoiceItemDTO build() {
        InvoiceItemDTO dto = new InvoiceItemDTO();
        dto.setId(id);
        dto.setDescription(description);
        dto.setItemType(ItemType.EMERGENCY_SERVICE);
        dto.setQuantity(quantity);
        dto.setUnitPrice(unitPrice);
        dto.setTotalPrice(total);
        return dto;
    }
}
