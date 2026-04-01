package edu.icet.model;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Medicine {
    private Integer id;
    private String name;
    private String brand;
    private String category;
    private Integer quantity;
    private Double unitPrice;
    private Integer reorderLevel;
    private LocalDate expiryDate;
    private Integer supplierId;
    private LocalDateTime createdAt;
}
