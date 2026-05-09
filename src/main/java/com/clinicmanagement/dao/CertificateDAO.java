package com.clinicmanagement.dao;

import com.clinicmanagement.db.DB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CertificateDAO {
    public void insert(int consultationId, String type, String startDate, int durationDays, String description) throws SQLException {
        String sql = "INSERT INTO certificates(id_consultation, certificate_type, start_date, duration_days, description) VALUES(?,?,?,?,?)";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, consultationId);
            ps.setString(2, type);
            ps.setString(3, startDate);
            ps.setInt(4, durationDays);
            ps.setString(5, description);
            ps.executeUpdate();
        }
    }

    public void update(int id, int consultationId, String type, String startDate, int durationDays, String description) throws SQLException {
        String sql = "UPDATE certificates SET id_consultation=?, certificate_type=?, start_date=?, duration_days=?, description=? WHERE id_certificate=?";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, consultationId);
            ps.setString(2, type);
            ps.setString(3, startDate);
            ps.setInt(4, durationDays);
            ps.setString(5, description);
            ps.setInt(6, id);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement("DELETE FROM certificates WHERE id_certificate=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Object[]> search(String keyword) throws SQLException {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT ce.id_certificate, ce.id_consultation, ce.certificate_type, ce.start_date, ce.duration_days, CONCAT(p.first_name,' ',p.last_name) patient, ce.description " +
                "FROM certificates ce JOIN consultations c ON c.id_consultation=ce.id_consultation JOIN patients p ON p.id_patient=c.id_patient " +
                "WHERE p.last_name LIKE ? OR p.first_name LIKE ? OR ce.certificate_type LIKE ? OR ce.description LIKE ? ORDER BY ce.id_certificate DESC";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            String k = "%" + keyword + "%";
            for (int i = 1; i <= 4; i++) ps.setString(i, k);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) rows.add(new Object[]{rs.getInt("id_certificate"), rs.getInt("id_consultation"), rs.getString("certificate_type"), rs.getDate("start_date").toString(), rs.getInt("duration_days"), rs.getString("patient"), rs.getString("description")});
            }
        }
        return rows;
    }

    public String printableCertificate(int id) throws SQLException {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT ce.*, p.last_name, p.first_name, p.birth_date FROM certificates ce " +
                "JOIN consultations c ON c.id_consultation=ce.id_consultation " +
                "JOIN patients p ON p.id_patient=c.id_patient WHERE ce.id_certificate=?";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    sb.append("MEDICAL CLINIC\n");
                    sb.append(rs.getString("certificate_type").toUpperCase()).append("\n\n");
                    sb.append("I, the undersigned doctor, certify that the patient: ");
                    sb.append(rs.getString("first_name")).append(" ").append(rs.getString("last_name"));
                    sb.append(", born on ").append(rs.getDate("birth_date"));
                    sb.append(", was examined on ").append(rs.getDate("start_date")).append(".\n\n");
                    sb.append("Description: ").append(rs.getString("description")).append("\n");
                    sb.append("Duration: ").append(rs.getInt("duration_days")).append(" days\n\n");
                    sb.append("Issued for all legal purposes.\n\nDoctor signature\n");
                }
            }
        }
        return sb.toString();
    }
}
