package edu.icet.service.interfaces;

import edu.icet.model.dto.SaleDTO;
import edu.icet.service.SuperService;

import java.time.LocalDate;
import java.util.List;

public interface SaleService extends SuperService {
    boolean saveSale(SaleDTO sale);
    boolean updateSale(SaleDTO sale);
    boolean deleteSale(Integer id);
    SaleDTO getSaleById(Integer id);
    List<SaleDTO> getAllSales();
    List<SaleDTO> getSalesByDate(LocalDate date);
    List<SaleDTO> searchSales(String invoice);
    String generateInvoiceNumber();
}
