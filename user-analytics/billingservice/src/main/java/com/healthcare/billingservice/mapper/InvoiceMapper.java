package com.healthcare.billingservice.mapper;

import com.healthcare.billingservice.dto.InvoiceDTO;
import com.healthcare.billingservice.dto.InvoiceItemDTO;
import com.healthcare.billingservice.entity.Invoice;
import com.healthcare.billingservice.entity.InvoiceItem;
import com.healthcare.billingservice.entity.InvoiceStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class InvoiceMapper {

    public InvoiceDTO toDTO(Invoice invoice) {
        if (invoice == null) return null;

        InvoiceDTO dto = new InvoiceDTO();
        dto.setId(invoice.getId());
        dto.setInvoiceNumber(invoice.getInvoiceNumber());
        dto.setPatientId(invoice.getPatientId());
        dto.setAppointmentId(invoice.getAppointmentId());
        dto.setDoctorId(invoice.getDoctorId());
        dto.setInvoiceDate(invoice.getInvoiceDate());
        dto.setDueDate(invoice.getDueDate());
        dto.setSubtotal(invoice.getSubtotal());
        dto.setTaxAmount(invoice.getTaxAmount());
        dto.setDiscountAmount(invoice.getDiscountAmount());
        dto.setTotalAmount(invoice.getTotalAmount());
        dto.setPaidAmount(invoice.getPaidAmount());
        dto.setBalanceDue(invoice.getBalanceDue());
        dto.setStatus(invoice.getStatus());
        dto.setPatientName(invoice.getPatientName());
        dto.setPatientEmail(invoice.getPatientEmail());
        dto.setPatientPhone(invoice.getPatientPhone());
        dto.setInsuranceProvider(invoice.getInsuranceProvider());
        dto.setInsurancePolicyNumber(invoice.getInsurancePolicyNumber());
        dto.setInsuranceClaimAmount(invoice.getInsuranceClaimAmount());
        dto.setNotes(invoice.getNotes());
        dto.setCreatedAt(invoice.getCreatedAt());
        dto.setUpdatedAt(invoice.getUpdatedAt());
        dto.setSentAt(invoice.getSentAt());
        dto.setPaidAt(invoice.getPaidAt());
        return dto;
    }

    public Invoice toEntity(InvoiceDTO dto) {
        if (dto == null) return null;

        Invoice invoice = new Invoice();
        invoice.setId(dto.getId());
        invoice.setInvoiceNumber(dto.getInvoiceNumber());
        invoice.setPatientId(dto.getPatientId());
        invoice.setAppointmentId(dto.getAppointmentId());
        invoice.setDoctorId(dto.getDoctorId());
        invoice.setInvoiceDate(dto.getInvoiceDate());
        invoice.setDueDate(dto.getDueDate());
        invoice.setTaxAmount(dto.getTaxAmount());
        invoice.setDiscountAmount(dto.getDiscountAmount());
        invoice.setStatus(dto.getStatus() != null ? dto.getStatus() : InvoiceStatus.DRAFT);
        invoice.setPatientName(dto.getPatientName());
        invoice.setPatientEmail(dto.getPatientEmail());
        invoice.setPatientPhone(dto.getPatientPhone());
        invoice.setInsuranceProvider(dto.getInsuranceProvider());
        invoice.setInsurancePolicyNumber(dto.getInsurancePolicyNumber());
        invoice.setInsuranceClaimAmount(dto.getInsuranceClaimAmount());
        invoice.setNotes(dto.getNotes());
        return invoice;
    }

    public InvoiceItem toInvoiceItemEntity(InvoiceItemDTO dto) {
        if (dto == null) return null;
        InvoiceItem item = new InvoiceItem();
        item.setId(dto.getId());
        // Do NOT set item.setInvoice(new Invoice());
        item.setDescription(dto.getDescription());
        item.setQuantity(dto.getQuantity());
        item.setUnitPrice(dto.getUnitPrice());
        item.setItemType(dto.getItemType());
        item.setTotalPrice(dto.getTotalPrice());
        return item;
    }

    public Invoice toEntitywithItems(InvoiceDTO dto) {
        if (dto == null) return null;

        Invoice invoice = new Invoice();
        invoice.setId(dto.getId());
        invoice.setInvoiceNumber(dto.getInvoiceNumber());
        invoice.setPatientId(dto.getPatientId());
        invoice.setAppointmentId(dto.getAppointmentId());
        invoice.setDoctorId(dto.getDoctorId());
        invoice.setInvoiceDate(dto.getInvoiceDate());
        invoice.setDueDate(dto.getDueDate());
        invoice.setTaxAmount(dto.getTaxAmount());
        invoice.setDiscountAmount(dto.getDiscountAmount());
        invoice.setStatus(dto.getStatus() != null ? dto.getStatus() : InvoiceStatus.DRAFT);

        // Map items and set parent invoice reference
        if (dto.getItems() != null) {
            invoice.setItems(dto.getItems().stream()
                    .map(this::toInvoiceItemEntity)
                    .collect(Collectors.toList()));
            for (InvoiceItem item : invoice.getItems()) {
                item.setInvoice(invoice);
            }
        } else {
            invoice.setItems(new ArrayList<>());
        }

        invoice.setPatientName(dto.getPatientName());
        invoice.setPatientEmail(dto.getPatientEmail());
        invoice.setPatientPhone(dto.getPatientPhone());
        invoice.setInsuranceProvider(dto.getInsuranceProvider());
        invoice.setInsurancePolicyNumber(dto.getInsurancePolicyNumber());
        invoice.setInsuranceClaimAmount(dto.getInsuranceClaimAmount());
        invoice.setNotes(dto.getNotes());
        return invoice;
    }

    public void updateEntityFromDTO(InvoiceDTO dto, Invoice invoice) {
        if (dto.getDueDate() != null) invoice.setDueDate(dto.getDueDate());
        if (dto.getTaxAmount() != null) invoice.setTaxAmount(dto.getTaxAmount());
        if (dto.getDiscountAmount() != null) invoice.setDiscountAmount(dto.getDiscountAmount());
        if (dto.getStatus() != null) invoice.setStatus(dto.getStatus());
        if (dto.getPatientName() != null) invoice.setPatientName(dto.getPatientName());
        if (dto.getPatientEmail() != null) invoice.setPatientEmail(dto.getPatientEmail());
        if (dto.getPatientPhone() != null) invoice.setPatientPhone(dto.getPatientPhone());
        if (dto.getNotes() != null) invoice.setNotes(dto.getNotes());
    }
}
