package edu.icet.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medicine {
    private Long id;
    private String name;
    private String brand;
    private String category;
    private int quantity;
    private BigDecimal unitPrice;
    private int reorderLevel;
    private LocalDate expiryDate;
    private Long supplierId;
    private LocalDateTime createdAt;
}
