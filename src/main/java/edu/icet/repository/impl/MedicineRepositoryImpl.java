package edu.icet.repository.impl;

import edu.icet.model.dto.MedicineDTO;
import edu.icet.repository.interfaces.MedicineRepository;
import edu.icet.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicineRepositoryImpl implements MedicineRepository {
    
    @Override
    public boolean save(MedicineDTO entity) {
        String sql = "INSERT INTO medicines (name, brand, category, quantity, unit_price, reorder_level, expiry_date, supplier_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            return CrudUtil.executeUpdate(sql, 
                entity.getName(), 
                entity.getBrand(), 
                entity.getCategory(), 
                entity.getQuantity(), 
                entity.getUnitPrice(), 
                entity.getReorderLevel(), 
                entity.getExpiryDate(), 
                entity.getSupplierId()
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean update(MedicineDTO entity) {
        String sql = "UPDATE medicines SET name=?, brand=?, category=?, quantity=?, unit_price=?, reorder_level=?, expiry_date=?, supplier_id=? WHERE id=?";
        try {
            return CrudUtil.executeUpdate(sql, 
                entity.getName(), 
                entity.getBrand(), 
                entity.getCategory(), 
                entity.getQuantity(), 
                entity.getUnitPrice(), 
                entity.getReorderLevel(), 
                entity.getExpiryDate(), 
                entity.getSupplierId(), 
                entity.getId()
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM medicines WHERE id=?";
        try {
            return CrudUtil.executeUpdate(sql, id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public MedicineDTO findById(Integer id) {
        String sql = "SELECT m.*, s.name as supplier_name FROM medicines m LEFT JOIN suppliers s ON m.supplier_id = s.id WHERE m.id=?";
        try {
            ResultSet rs = CrudUtil.executeQuery(sql, id);
            if (rs.next()) {
                return extractMedicineDTO(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<MedicineDTO> findAll() {
        String sql = "SELECT m.*, s.name as supplier_name FROM medicines m LEFT JOIN suppliers s ON m.supplier_id = s.id";
        List<MedicineDTO> list = new ArrayList<>();
        try {
            ResultSet rs = CrudUtil.executeQuery(sql);
            while (rs.next()) {
                list.add(extractMedicineDTO(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public List<MedicineDTO> searchByName(String name) {
        String sql = "SELECT m.*, s.name as supplier_name FROM medicines m LEFT JOIN suppliers s ON m.supplier_id = s.id WHERE m.name LIKE ?";
        List<MedicineDTO> list = new ArrayList<>();
        try {
            ResultSet rs = CrudUtil.executeQuery(sql, "%" + name + "%");
            while (rs.next()) {
                list.add(extractMedicineDTO(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public List<MedicineDTO> findLowStock() {
        String sql = "SELECT m.*, s.name as supplier_name FROM medicines m LEFT JOIN suppliers s ON m.supplier_id = s.id WHERE m.quantity <= m.reorder_level";
        List<MedicineDTO> list = new ArrayList<>();
        try {
            ResultSet rs = CrudUtil.executeQuery(sql);
            while (rs.next()) {
                list.add(extractMedicineDTO(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public List<MedicineDTO> findExpiring(int days) {
        String sql = "SELECT m.*, s.name as supplier_name FROM medicines m LEFT JOIN suppliers s ON m.supplier_id = s.id WHERE m.expiry_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL ? DAY)";
        List<MedicineDTO> list = new ArrayList<>();
        try {
            ResultSet rs = CrudUtil.executeQuery(sql, days);
            while (rs.next()) {
                list.add(extractMedicineDTO(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    private MedicineDTO extractMedicineDTO(ResultSet rs) throws SQLException {
        MedicineDTO dto = new MedicineDTO();
        dto.setId(rs.getInt("id"));
        dto.setName(rs.getString("name"));
        dto.setBrand(rs.getString("brand"));
        dto.setCategory(rs.getString("category"));
        dto.setQuantity(rs.getInt("quantity"));
        dto.setUnitPrice(rs.getDouble("unit_price"));
        dto.setReorderLevel(rs.getInt("reorder_level"));
        
        // Handle null expiry_date
        java.sql.Date expiryDate = rs.getDate("expiry_date");
        dto.setExpiryDate(expiryDate != null ? expiryDate.toLocalDate() : null);
        
        dto.setSupplierId(rs.getInt("supplier_id"));
        
        // Handle null supplier_name
        String supplierName = rs.getString("supplier_name");
        dto.setSupplierName(supplierName != null ? supplierName : "Unknown");
        
        return dto;
    }
}
