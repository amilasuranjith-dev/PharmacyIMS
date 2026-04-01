package edu.icet.util;

import edu.icet.db.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CrudUtil {
    
    public static ResultSet executeQuery(String sql, Object... params) throws SQLException {
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        
        for (int i = 0; i < params.length; i++) {
            pstm.setObject(i + 1, params[i]);
        }
        
        // Note: ResultSet is not closed here as it needs to be used by the caller
        // Caller is responsible for closing the ResultSet and PreparedStatement
        return pstm.executeQuery();
    }
    
    public static boolean executeUpdate(String sql, Object... params) throws SQLException {
        PreparedStatement pstm = null;
        try {
            pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
            
            for (int i = 0; i < params.length; i++) {
                pstm.setObject(i + 1, params[i]);
            }
            
            return pstm.executeUpdate() > 0;
        } finally {
            if (pstm != null) {
                try {
                    pstm.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
