package com.clinicmanagement.dao;

import com.clinicmanagement.db.DB;
import com.clinicmanagement.model.User;

import java.sql.*;

public class AuthDAO {
    public User login(String username, String password) throws SQLException {
        String sql = "SELECT id_user, last_name, first_name, username, role FROM users WHERE username=? AND password=?";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("id_user"), rs.getString("last_name"), rs.getString("first_name"), rs.getString("username"), rs.getString("role"));
                }
                return null;
            }
        }
    }
}
