package edu.icet.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SaleItem {
    private Integer id;
    private Integer saleId;
    private Integer medicineId;
    private Integer quantity;
    private Double unitPrice;
    private Double subtotal;
}
