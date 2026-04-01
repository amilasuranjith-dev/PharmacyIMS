package edu.icet.repository.interfaces;

import edu.icet.model.dto.SaleDTO;
import edu.icet.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends CrudRepository<SaleDTO, Integer> {
    List<SaleDTO> findByDate(LocalDate date);
    List<SaleDTO> searchByInvoice(String invoice);
    SaleDTO findByInvoiceNumber(String invoiceNumber);
    String findLastInvoiceNumber();
}
