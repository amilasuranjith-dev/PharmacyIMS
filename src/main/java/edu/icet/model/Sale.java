package edu.icet.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Sale {
    private Integer id;
    private String invoiceNumber;
    private Double totalAmount;
    private Double discount;
    private Double paidAmount;
    private LocalDateTime saleDate;
    private String notes;
}
