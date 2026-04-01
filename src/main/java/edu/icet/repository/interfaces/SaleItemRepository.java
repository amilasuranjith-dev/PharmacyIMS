package edu.icet.repository.interfaces;

import edu.icet.model.dto.SaleItemDTO;
import edu.icet.repository.CrudRepository;

import java.util.List;

public interface SaleItemRepository extends CrudRepository<SaleItemDTO, Integer> {
    List<SaleItemDTO> findBySaleId(Integer saleId);
}
