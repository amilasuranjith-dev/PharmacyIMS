package edu.icet.service.impl;

import edu.icet.model.dto.DashboardDTO;
import edu.icet.model.dto.MedicineDTO;
import edu.icet.model.dto.SaleDTO;
import edu.icet.repository.interfaces.MedicineRepository;
import edu.icet.repository.RepositoryFactory;
import edu.icet.repository.interfaces.SaleRepository;
import edu.icet.service.interfaces.DashboardService;
import edu.icet.util.RepositoryType;

import java.time.LocalDate;
import java.util.List;

public class DashboardServiceImpl implements DashboardService {
    private final MedicineRepository medicineRepository;
    private final SaleRepository saleRepository;
    
    public DashboardServiceImpl() {
        this.medicineRepository = RepositoryFactory.getInstance().getRepository(RepositoryType.MEDICINE);
        this.saleRepository = RepositoryFactory.getInstance().getRepository(RepositoryType.SALE);
    }
    
    @Override
    public DashboardDTO getDashboardData() {
        DashboardDTO dashboard = new DashboardDTO();
        
        List<MedicineDTO> lowStock = medicineRepository.findLowStock();
        List<MedicineDTO> expiring = medicineRepository.findExpiring(30);
        List<MedicineDTO> allMedicines = medicineRepository.findAll();
        List<SaleDTO> todaySales = saleRepository.findByDate(LocalDate.now());
        
        dashboard.setLowStockCount(lowStock.size());
        dashboard.setExpiringCount(expiring.size());
        dashboard.setTotalMedicines(allMedicines.size());
        
        double totalSales = todaySales.stream()
            .mapToDouble(SaleDTO::getTotalAmount)
            .sum();
        dashboard.setTotalSalesToday(totalSales);
        
        return dashboard;
    }
    
    @Override
    public List<MedicineDTO> getLowStockMedicines() {
        return medicineRepository.findLowStock();
    }
    
    @Override
    public List<MedicineDTO> getExpiringMedicines() {
        return medicineRepository.findExpiring(30);
    }
}
