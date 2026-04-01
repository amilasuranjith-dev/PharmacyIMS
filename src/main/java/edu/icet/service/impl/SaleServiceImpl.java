package edu.icet.service.impl;

import edu.icet.model.dto.SaleDTO;
import edu.icet.model.dto.SaleItemDTO;
import edu.icet.repository.RepositoryFactory;
import edu.icet.repository.interfaces.SaleItemRepository;
import edu.icet.repository.interfaces.SaleRepository;
import edu.icet.service.interfaces.SaleService;
import edu.icet.util.RepositoryType;

import java.time.LocalDate;
import java.util.List;

public class SaleServiceImpl implements SaleService {
    private final SaleRepository saleRepository;
    private final SaleItemRepository saleItemRepository;
    
    public SaleServiceImpl() {
        this.saleRepository = RepositoryFactory.getInstance().getRepository(RepositoryType.SALE);
        this.saleItemRepository = RepositoryFactory.getInstance().getRepository(RepositoryType.SALE_ITEM);
    }
    
    @Override
    public boolean saveSale(SaleDTO sale) {
        boolean saved = saleRepository.save(sale);
        if (saved && sale.getItems() != null && !sale.getItems().isEmpty()) {
            SaleDTO savedSale = saleRepository.findByInvoiceNumber(sale.getInvoiceNumber());
            if (savedSale != null) {
                for (SaleItemDTO item : sale.getItems()) {
                    item.setSaleId(savedSale.getId());
                    saleItemRepository.save(item);
                }
            }
        }
        return saved;
    }
    
    @Override
    public boolean updateSale(SaleDTO sale) {
        return saleRepository.update(sale);
    }
    
    @Override
    public boolean deleteSale(Integer id) {
        return saleRepository.delete(id);
    }
    
    @Override
    public SaleDTO getSaleById(Integer id) {
        SaleDTO sale = saleRepository.findById(id);
        if (sale != null) {
            List<SaleItemDTO> items = saleItemRepository.findBySaleId(id);
            sale.setItems(items);
        }
        return sale;
    }
    
    @Override
    public List<SaleDTO> getAllSales() {
        return saleRepository.findAll();
    }
    
    @Override
    public List<SaleDTO> getSalesByDate(LocalDate date) {
        return saleRepository.findByDate(date);
    }
    
    @Override
    public List<SaleDTO> searchSales(String invoice) {
        return saleRepository.searchByInvoice(invoice);
    }
    
    @Override
    public String generateInvoiceNumber() {
        final String prefix = "INV";

        String last = saleRepository.findLastInvoiceNumber();
        int nextNumber = 1;

        if (last != null && last.startsWith(prefix)) {
            String numericPart = last.substring(prefix.length()).trim();
            try {
                nextNumber = Integer.parseInt(numericPart) + 1;
            } catch (NumberFormatException ignored) {
                // If existing data doesn't follow INV### format, start fresh.
                nextNumber = 1;
            }
        }

        return String.format("%s%03d", prefix, nextNumber);
    }
}
