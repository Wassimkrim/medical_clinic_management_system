package com.clinicmanagement.dao;

import com.clinicmanagement.db.DB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultationDAO {
    public void insert(int patientId, int doctorId, String date, String symptoms, String diagnosis, String treatment, String report) throws SQLException {
        String sql = "INSERT INTO consultations(id_patient, id_doctor, consultation_date, symptoms, diagnosis, treatment, report) VALUES(?,?,?,?,?,?,?)";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ps.setInt(2, doctorId);
            ps.setString(3, date);
            ps.setString(4, symptoms);
            ps.setString(5, diagnosis);
            ps.setString(6, treatment);
            ps.setString(7, report);
            ps.executeUpdate();
        }
    }

    public void update(int id, int patientId, String date, String symptoms, String diagnosis, String treatment, String report) throws SQLException {
        String sql = "UPDATE consultations SET id_patient=?, consultation_date=?, symptoms=?, diagnosis=?, treatment=?, report=? WHERE id_consultation=?";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ps.setString(2, date);
            ps.setString(3, symptoms);
            ps.setString(4, diagnosis);
            ps.setString(5, treatment);
            ps.setString(6, report);
            ps.setInt(7, id);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement("DELETE FROM consultations WHERE id_consultation=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Object[]> search(String keyword) throws SQLException {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT c.id_consultation, p.id_patient, CONCAT(p.first_name,' ',p.last_name) patient, c.consultation_date, c.symptoms, c.diagnosis, c.treatment, c.report " +
                "FROM consultations c JOIN patients p ON p.id_patient=c.id_patient " +
                "WHERE p.last_name LIKE ? OR p.first_name LIKE ? OR c.consultation_date LIKE ? OR c.diagnosis LIKE ? OR c.symptoms LIKE ? " +
                "ORDER BY c.id_consultation DESC";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            String k = "%" + keyword + "%";
            for (int i = 1; i <= 5; i++) ps.setString(i, k);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Object[]{rs.getInt("id_consultation"), rs.getInt("id_patient"), rs.getString("patient"), rs.getDate("consultation_date").toString(), rs.getString("symptoms"), rs.getString("diagnosis"), rs.getString("treatment"), rs.getString("report")});
                }
            }
        }
        return rows;
    }
}
