package com.clinicmanagement.dao;

import com.clinicmanagement.db.DB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDAO {
    public void insert(int patientId, int secretaryId, String date, String time, String reason, String status) throws SQLException {
        String sql = "INSERT INTO appointments(id_patient, id_secretary, appointment_date, appointment_time, reason, status) VALUES(?,?,?,?,?,?)";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ps.setInt(2, secretaryId);
            ps.setString(3, date);
            ps.setString(4, time);
            ps.setString(5, reason);
            ps.setString(6, status);
            ps.executeUpdate();
        }
    }

    public void update(int id, int patientId, String date, String time, String reason, String status) throws SQLException {
        String sql = "UPDATE appointments SET id_patient=?, appointment_date=?, appointment_time=?, reason=?, status=? WHERE id_appointment=?";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ps.setString(2, date);
            ps.setString(3, time);
            ps.setString(4, reason);
            ps.setString(5, status);
            ps.setInt(6, id);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement("DELETE FROM appointments WHERE id_appointment=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Object[]> search(String keyword) throws SQLException {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT a.id_appointment, p.id_patient, CONCAT(p.first_name,' ',p.last_name) patient, a.appointment_date, a.appointment_time, a.reason, a.status " +
                "FROM appointments a JOIN patients p ON p.id_patient=a.id_patient " +
                "WHERE p.last_name LIKE ? OR p.first_name LIKE ? OR a.appointment_date LIKE ? OR a.status LIKE ? OR a.reason LIKE ? " +
                "ORDER BY a.appointment_date DESC, a.appointment_time DESC";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            String k = "%" + keyword + "%";
            for (int i = 1; i <= 5; i++) ps.setString(i, k);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Object[]{rs.getInt("id_appointment"), rs.getInt("id_patient"), rs.getString("patient"), rs.getDate("appointment_date").toString(), rs.getTime("appointment_time").toString(), rs.getString("reason"), rs.getString("status")});
                }
            }
        }
        return rows;
    }
}
