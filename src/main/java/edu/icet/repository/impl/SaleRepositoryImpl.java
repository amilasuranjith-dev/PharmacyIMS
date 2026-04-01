package edu.icet.repository.impl;

import edu.icet.model.dto.SaleDTO;
import edu.icet.repository.interfaces.SaleRepository;
import edu.icet.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SaleRepositoryImpl implements SaleRepository {
    
    @Override
    public boolean save(SaleDTO entity) {
        String sql = "INSERT INTO sales (invoice_number, total_amount, discount, paid_amount, notes) VALUES (?, ?, ?, ?, ?)";
        try {
            return CrudUtil.executeUpdate(sql, 
                entity.getInvoiceNumber(), 
                entity.getTotalAmount(), 
                entity.getDiscount(), 
                entity.getPaidAmount(), 
                entity.getNotes()
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean update(SaleDTO entity) {
        String sql = "UPDATE sales SET invoice_number=?, total_amount=?, discount=?, paid_amount=?, notes=? WHERE id=?";
        try {
            return CrudUtil.executeUpdate(sql, 
                entity.getInvoiceNumber(), 
                entity.getTotalAmount(), 
                entity.getDiscount(), 
                entity.getPaidAmount(), 
                entity.getNotes(), 
                entity.getId()
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM sales WHERE id=?";
        try {
            return CrudUtil.executeUpdate(sql, id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public SaleDTO findById(Integer id) {
        String sql = "SELECT * FROM sales WHERE id=?";
        try {
            ResultSet rs = CrudUtil.executeQuery(sql, id);
            if (rs.next()) {
                return extractSaleDTO(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<SaleDTO> findAll() {
        String sql = "SELECT * FROM sales ORDER BY id ASC";
        List<SaleDTO> list = new ArrayList<>();
        try {
            ResultSet rs = CrudUtil.executeQuery(sql);
            while (rs.next()) {
                list.add(extractSaleDTO(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public List<SaleDTO> findByDate(LocalDate date) {
        String sql = "SELECT * FROM sales WHERE DATE(sale_date) = ?";
        List<SaleDTO> list = new ArrayList<>();
        try {
            ResultSet rs = CrudUtil.executeQuery(sql, date);
            while (rs.next()) {
                list.add(extractSaleDTO(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public List<SaleDTO> searchByInvoice(String invoice) {
        String sql = "SELECT * FROM sales WHERE invoice_number LIKE ?";
        List<SaleDTO> list = new ArrayList<>();
        try {
            ResultSet rs = CrudUtil.executeQuery(sql, "%" + invoice + "%");
            while (rs.next()) {
                list.add(extractSaleDTO(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public SaleDTO findByInvoiceNumber(String invoiceNumber) {
        String sql = "SELECT * FROM sales WHERE invoice_number=?";
        try {
            ResultSet rs = CrudUtil.executeQuery(sql, invoiceNumber);
            if (rs.next()) {
                return extractSaleDTO(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public String findLastInvoiceNumber() {
        // Order by invoice_number descending; assumes format like INV001
        String sql = "SELECT invoice_number FROM sales WHERE invoice_number LIKE 'INV%' ORDER BY invoice_number DESC LIMIT 1";
        try {
            ResultSet rs = CrudUtil.executeQuery(sql);
            if (rs.next()) {
                return rs.getString("invoice_number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private SaleDTO extractSaleDTO(ResultSet rs) throws SQLException {
        SaleDTO dto = new SaleDTO();
        dto.setId(rs.getInt("id"));
        dto.setInvoiceNumber(rs.getString("invoice_number"));
        dto.setTotalAmount(rs.getDouble("total_amount"));
        dto.setDiscount(rs.getDouble("discount"));
        dto.setPaidAmount(rs.getDouble("paid_amount"));
        
        // Handle null sale_date
        java.sql.Timestamp timestamp = rs.getTimestamp("sale_date");
        dto.setSaleDate(timestamp != null ? timestamp.toLocalDateTime() : java.time.LocalDateTime.now());
        
        dto.setNotes(rs.getString("notes"));
        return dto;
    }
}
