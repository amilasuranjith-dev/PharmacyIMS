package edu.icet.service.impl;

import edu.icet.model.dto.MedicineDTO;
import edu.icet.repository.interfaces.MedicineRepository;
import edu.icet.repository.RepositoryFactory;
import edu.icet.service.interfaces.MedicineService;
import edu.icet.util.RepositoryType;

import java.util.List;

public class MedicineServiceImpl implements MedicineService {
    private final MedicineRepository repository;
    
    public MedicineServiceImpl() {
        this.repository = RepositoryFactory.getInstance().getRepository(RepositoryType.MEDICINE);
    }
    
    @Override
    public boolean saveMedicine(MedicineDTO medicine) {
        return repository.save(medicine);
    }
    
    @Override
    public boolean updateMedicine(MedicineDTO medicine) {
        return repository.update(medicine);
    }
    
    @Override
    public boolean deleteMedicine(Integer id) {
        return repository.delete(id);
    }
    
    @Override
    public MedicineDTO getMedicineById(Integer id) {
        return repository.findById(id);
    }
    
    @Override
    public List<MedicineDTO> getAllMedicines() {
        return repository.findAll();
    }
    
    @Override
    public List<MedicineDTO> searchMedicines(String name) {
        return repository.searchByName(name);
    }
    
    @Override
    public List<MedicineDTO> getLowStockMedicines() {
        return repository.findLowStock();
    }
    
    @Override
    public List<MedicineDTO> getExpiringMedicines(int days) {
        return repository.findExpiring(days);
    }
}
