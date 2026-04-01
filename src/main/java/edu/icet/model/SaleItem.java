package edu.icet.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleItem {
    private Long id;
    private Long saleId;
    private Long medicineId;
    private transient String medicineName;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
}
