package edu.icet.repository.impl;

import edu.icet.model.dto.SupplierDTO;
import edu.icet.repository.interfaces.SupplierRepository;
import edu.icet.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierRepositoryImpl implements SupplierRepository {
    
    @Override
    public boolean save(SupplierDTO entity) {
        String sql = "INSERT INTO suppliers (name, contact_person, phone, email, address) VALUES (?, ?, ?, ?, ?)";
        try {
            return CrudUtil.executeUpdate(sql, 
                entity.getName(), 
                entity.getContactPerson(), 
                entity.getPhone(), 
                entity.getEmail(), 
                entity.getAddress()
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean update(SupplierDTO entity) {
        String sql = "UPDATE suppliers SET name=?, contact_person=?, phone=?, email=?, address=? WHERE id=?";
        try {
            return CrudUtil.executeUpdate(sql, 
                entity.getName(), 
                entity.getContactPerson(), 
                entity.getPhone(), 
                entity.getEmail(), 
                entity.getAddress(), 
                entity.getId()
            );
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM suppliers WHERE id=?";
        try {
            return CrudUtil.executeUpdate(sql, id);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public SupplierDTO findById(Integer id) {
        String sql = "SELECT * FROM suppliers WHERE id=?";
        try {
            ResultSet rs = CrudUtil.executeQuery(sql, id);
            if (rs.next()) {
                return extractSupplierDTO(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Override
    public List<SupplierDTO> findAll() {
        String sql = "SELECT * FROM suppliers";
        List<SupplierDTO> list = new ArrayList<>();
        try {
            ResultSet rs = CrudUtil.executeQuery(sql);
            while (rs.next()) {
                list.add(extractSupplierDTO(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    @Override
    public List<SupplierDTO> searchByName(String name) {
        String sql = "SELECT * FROM suppliers WHERE name LIKE ?";
        List<SupplierDTO> list = new ArrayList<>();
        try {
            ResultSet rs = CrudUtil.executeQuery(sql, "%" + name + "%");
            while (rs.next()) {
                list.add(extractSupplierDTO(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    private SupplierDTO extractSupplierDTO(ResultSet rs) throws SQLException {
        SupplierDTO dto = new SupplierDTO();
        dto.setId(rs.getInt("id"));
        dto.setName(rs.getString("name"));
        dto.setContactPerson(rs.getString("contact_person"));
        dto.setPhone(rs.getString("phone"));
        dto.setEmail(rs.getString("email"));
        dto.setAddress(rs.getString("address"));
        return dto;
    }
}
