package edu.icet.service.interfaces;

import edu.icet.model.dto.DashboardDTO;
import edu.icet.model.dto.MedicineDTO;
import edu.icet.service.SuperService;

import java.util.List;

public interface DashboardService extends SuperService {
    DashboardDTO getDashboardData();
    List<MedicineDTO> getLowStockMedicines();
    List<MedicineDTO> getExpiringMedicines();
}
