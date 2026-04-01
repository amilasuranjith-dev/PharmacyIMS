package edu.icet.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SaleItemDTO {
    private Integer id;
    private Integer saleId;
    private Integer medicineId;
    private String medicineName;
    private Integer quantity;
    private Double unitPrice;
    private Double subtotal;
}
