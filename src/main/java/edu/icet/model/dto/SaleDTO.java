package edu.icet.model.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SaleDTO {
    private Integer id;
    private String invoiceNumber;
    private Double totalAmount;
    private Double discount;
    private Double paidAmount;
    private LocalDateTime saleDate;
    private String notes;
    private List<SaleItemDTO> items = new ArrayList<>();
}
