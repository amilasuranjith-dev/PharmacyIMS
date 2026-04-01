package edu.icet.service.interfaces;

import edu.icet.model.dto.MedicineDTO;
import edu.icet.service.SuperService;

import java.util.List;

public interface MedicineService extends SuperService {
    boolean saveMedicine(MedicineDTO medicine);
    boolean updateMedicine(MedicineDTO medicine);
    boolean deleteMedicine(Integer id);
    MedicineDTO getMedicineById(Integer id);
    List<MedicineDTO> getAllMedicines();
    List<MedicineDTO> searchMedicines(String name);
    List<MedicineDTO> getLowStockMedicines();
    List<MedicineDTO> getExpiringMedicines(int days);
}
