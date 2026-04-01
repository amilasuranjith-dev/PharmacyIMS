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
public class DashboardSummary {
    private int totalMedicines;
    private int lowStockCount;
    private int expiringCount;
    private BigDecimal todaySales;
}
