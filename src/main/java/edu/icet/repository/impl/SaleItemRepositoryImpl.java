package edu.icet.repository.impl;

import edu.icet.model.dto.SaleItemDTO;
import edu.icet.repository.interfaces.SaleItemRepository;
import edu.icet.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SaleItemRepositoryImpl implements SaleItemRepository {
    
    @Override
    public boolean save(SaleItemDTO entity) {
        String sql = "INSERT INTO sale_items (sale_id, medicine_id, quantity, unit_price, subtotal) VALUES (?, ?, ?, ?, ?)";
        try {
            return CrudUtil.executeUpdate(sql, 
                entity.getSaleId(), 
                entity.getMedicineId(), 
                entity.getQuantity(), 
                entity.getUnitPrice(), 
                entity.getSubtotal()
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean update(SaleItemDTO entity) {
        String sql = "UPDATE sale_items SET sale_id=?, medicine_id=?, quantity=?, unit_price=?, subtotal=? WHERE id=?";
        try {
            return CrudUtil.executeUpdate(sql, 
                entity.getSaleId(), 
                entity.getMedicineId(), 
                entity.getQuantity(), 
                entity.getUnitPrice(), 
                entity.getSubtotal(), 
                entity.getId()
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM sale_items WHERE id=?";
        try {
            return CrudUtil.executeUpdate(sql, id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public SaleItemDTO findById(Integer id) {
        String sql = "SELECT si.*, m.name as medicine_name FROM sale_items si LEFT JOIN medicines m ON si.medicine_id = m.id WHERE si.id=?";
        try {
            ResultSet rs = CrudUtil.executeQuery(sql, id);
            if (rs.next()) {
                return extractSaleItemDTO(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<SaleItemDTO> findAll() {
        String sql = "SELECT si.*, m.name as medicine_name FROM sale_items si LEFT JOIN medicines m ON si.medicine_id = m.id";
        List<SaleItemDTO> list = new ArrayList<>();
        try {
            ResultSet rs = CrudUtil.executeQuery(sql);
            while (rs.next()) {
                list.add(extractSaleItemDTO(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public List<SaleItemDTO> findBySaleId(Integer saleId) {
        String sql = "SELECT si.*, m.name as medicine_name FROM sale_items si LEFT JOIN medicines m ON si.medicine_id = m.id WHERE si.sale_id=?";
        List<SaleItemDTO> list = new ArrayList<>();
        try {
            ResultSet rs = CrudUtil.executeQuery(sql, saleId);
            while (rs.next()) {
                list.add(extractSaleItemDTO(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    private SaleItemDTO extractSaleItemDTO(ResultSet rs) throws SQLException {
        SaleItemDTO dto = new SaleItemDTO();
        dto.setId(rs.getInt("id"));
        dto.setSaleId(rs.getInt("sale_id"));
        dto.setMedicineId(rs.getInt("medicine_id"));
        dto.setMedicineName(rs.getString("medicine_name"));
        dto.setQuantity(rs.getInt("quantity"));
        dto.setUnitPrice(rs.getDouble("unit_price"));
        dto.setSubtotal(rs.getDouble("subtotal"));
        return dto;
    }
}
