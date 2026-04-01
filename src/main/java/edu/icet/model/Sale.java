package edu.icet.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sale {
    private Long id;
    private String invoiceNumber;
    private BigDecimal totalAmount;
    private BigDecimal discount;
    private BigDecimal paidAmount;
    private LocalDateTime saleDate;
    private String notes;
    private List<SaleItem> items;
}
