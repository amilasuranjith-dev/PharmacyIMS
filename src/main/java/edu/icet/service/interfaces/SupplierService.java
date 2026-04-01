package edu.icet.service.interfaces;

import edu.icet.model.dto.SupplierDTO;
import edu.icet.service.SuperService;

import java.util.List;

public interface SupplierService extends SuperService {
    boolean saveSupplier(SupplierDTO supplier);
    boolean updateSupplier(SupplierDTO supplier);
    boolean deleteSupplier(Integer id);
    SupplierDTO getSupplierById(Integer id);
    List<SupplierDTO> getAllSuppliers();
    List<SupplierDTO> searchSuppliers(String name);
}
