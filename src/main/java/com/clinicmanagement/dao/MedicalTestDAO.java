package com.clinicmanagement.dao;

import com.clinicmanagement.db.DB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicalTestDAO {
    public void insert(int consultationId, String type, String date, String result) throws SQLException {
        String sql = "INSERT INTO medical_tests(id_consultation, test_type, result, test_date) VALUES(?,?,?,?)";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, consultationId);
            ps.setString(2, type);
            ps.setString(3, result);
            ps.setString(4, date);
            ps.executeUpdate();
        }
    }

    public void update(int id, int consultationId, String type, String date, String result) throws SQLException {
        String sql = "UPDATE medical_tests SET id_consultation=?, test_type=?, result=?, test_date=? WHERE id_test=?";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, consultationId);
            ps.setString(2, type);
            ps.setString(3, result);
            ps.setString(4, date);
            ps.setInt(5, id);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement("DELETE FROM medical_tests WHERE id_test=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Object[]> search(String keyword) throws SQLException {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT mt.id_test, mt.id_consultation, mt.test_type, mt.test_date, CONCAT(p.first_name,' ',p.last_name) patient, mt.result " +
                "FROM medical_tests mt JOIN consultations c ON c.id_consultation=mt.id_consultation " +
                "JOIN patients p ON p.id_patient=c.id_patient " +
                "WHERE p.last_name LIKE ? OR p.first_name LIKE ? OR mt.test_type LIKE ? OR mt.result LIKE ? OR mt.test_date LIKE ? " +
                "ORDER BY mt.id_test DESC";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            String k = "%" + keyword + "%";
            for (int i = 1; i <= 5; i++) ps.setString(i, k);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) rows.add(new Object[]{rs.getInt("id_test"), rs.getInt("id_consultation"), rs.getString("test_type"), rs.getDate("test_date").toString(), rs.getString("patient"), rs.getString("result")});
            }
        }
        return rows;
    }

    public String printableTest(int id) throws SQLException {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT mt.*, p.last_name, p.first_name, p.birth_date, c.consultation_date " +
                "FROM medical_tests mt JOIN consultations c ON c.id_consultation=mt.id_consultation " +
                "JOIN patients p ON p.id_patient=c.id_patient WHERE mt.id_test=?";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    sb.append("MEDICAL CLINIC\n");
                    sb.append("MEDICAL TEST No: ").append(rs.getInt("id_test")).append("\n\n");
                    sb.append("Patient: ").append(rs.getString("first_name")).append(" ").append(rs.getString("last_name")).append("\n");
                    sb.append("Birth date: ").append(rs.getDate("birth_date")).append("\n");
                    sb.append("Consultation date: ").append(rs.getDate("consultation_date")).append("\n");
                    sb.append("Test date: ").append(rs.getDate("test_date")).append("\n");
                    sb.append("Test type: ").append(rs.getString("test_type")).append("\n\n");
                    sb.append("Result:\n").append(rs.getString("result")).append("\n\n");
                    sb.append("Doctor signature\n");
                }
            }
        }
        return sb.toString();
    }
}
