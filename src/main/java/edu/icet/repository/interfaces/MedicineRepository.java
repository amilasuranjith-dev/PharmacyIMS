package edu.icet.repository.interfaces;

import edu.icet.model.dto.MedicineDTO;
import edu.icet.repository.CrudRepository;

import java.util.List;

public interface MedicineRepository extends CrudRepository<MedicineDTO, Integer> {
    List<MedicineDTO> searchByName(String name);
    List<MedicineDTO> findLowStock();
    List<MedicineDTO> findExpiring(int days);
}
