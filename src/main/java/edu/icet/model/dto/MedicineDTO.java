package edu.icet.model.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MedicineDTO {
    private Integer id;
    private String name;
    private String brand;
    private String category;
    private Integer quantity;
    private Double unitPrice;
    private Integer reorderLevel;
    private LocalDate expiryDate;
    private Integer supplierId;
    private String supplierName;
}
