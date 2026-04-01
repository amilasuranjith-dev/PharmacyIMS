package edu.icet.model.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DashboardDTO {
    private Integer lowStockCount;
    private Integer expiringCount;
    private Double totalSalesToday;
    private Integer totalMedicines;
}
