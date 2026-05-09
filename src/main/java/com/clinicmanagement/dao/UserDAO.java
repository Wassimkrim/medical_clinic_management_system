package com.clinicmanagement.dao;

import com.clinicmanagement.db.DB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    public void insert(String lastName, String firstName, String username, String password, String role) throws SQLException {
        String sql = "INSERT INTO users(last_name, first_name, username, password, role) VALUES(?,?,?,?,?)";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, lastName);
            ps.setString(2, firstName);
            ps.setString(3, username);
            ps.setString(4, password);
            ps.setString(5, role);
            ps.executeUpdate();
        }
    }

    public void update(int id, String lastName, String firstName, String username, String password, String role) throws SQLException {
        String sql = "UPDATE users SET last_name=?, first_name=?, username=?, password=?, role=? WHERE id_user=?";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, lastName);
            ps.setString(2, firstName);
            ps.setString(3, username);
            ps.setString(4, password);
            ps.setString(5, role);
            ps.setInt(6, id);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement("DELETE FROM users WHERE id_user=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Object[]> search(String keyword) throws SQLException {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT id_user, last_name, first_name, username, role FROM users WHERE last_name LIKE ? OR first_name LIKE ? OR username LIKE ? OR role LIKE ? ORDER BY id_user DESC";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            String k = "%" + keyword + "%";
            for (int i = 1; i <= 4; i++) ps.setString(i, k);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) rows.add(new Object[]{rs.getInt("id_user"), rs.getString("last_name"), rs.getString("first_name"), rs.getString("username"), rs.getString("role")});
            }
        }
        return rows;
    }
}
