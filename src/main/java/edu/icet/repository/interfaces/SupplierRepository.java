package edu.icet.repository.interfaces;

import edu.icet.model.dto.SupplierDTO;
import edu.icet.repository.CrudRepository;

import java.util.List;

public interface SupplierRepository extends CrudRepository<SupplierDTO, Integer> {
    List<SupplierDTO> searchByName(String name);
}
