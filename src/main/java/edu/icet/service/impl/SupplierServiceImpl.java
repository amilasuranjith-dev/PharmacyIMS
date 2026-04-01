package edu.icet.service.impl;

import edu.icet.model.dto.SupplierDTO;
import edu.icet.repository.RepositoryFactory;
import edu.icet.repository.interfaces.SupplierRepository;
import edu.icet.service.interfaces.SupplierService;
import edu.icet.util.RepositoryType;

import java.util.List;

public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository repository;
    
    public SupplierServiceImpl() {
        this.repository = RepositoryFactory.getInstance().getRepository(RepositoryType.SUPPLIER);
    }
    
    @Override
    public boolean saveSupplier(SupplierDTO supplier) {
        return repository.save(supplier);
    }
    
    @Override
    public boolean updateSupplier(SupplierDTO supplier) {
        return repository.update(supplier);
    }
    
    @Override
    public boolean deleteSupplier(Integer id) {
        return repository.delete(id);
    }
    
    @Override
    public SupplierDTO getSupplierById(Integer id) {
        return repository.findById(id);
    }
    
    @Override
    public List<SupplierDTO> getAllSuppliers() {
        return repository.findAll();
    }
    
    @Override
    public List<SupplierDTO> searchSuppliers(String name) {
        return repository.searchByName(name);
    }
}
