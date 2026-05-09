package com.clinicmanagement.dao;

import com.clinicmanagement.db.DB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDAO {
    public int createPrescription(int consultationId, String date, String note) throws SQLException {
        String sql = "INSERT INTO prescriptions(id_consultation, prescription_date, note) VALUES(?,?,?)";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, consultationId);
            ps.setString(2, date);
            ps.setString(3, note);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public void updatePrescription(int id, int consultationId, String date, String note) throws SQLException {
        String sql = "UPDATE prescriptions SET id_consultation=?, prescription_date=?, note=? WHERE id_prescription=?";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, consultationId);
            ps.setString(2, date);
            ps.setString(3, note);
            ps.setInt(4, id);
            ps.executeUpdate();
        }
    }

    public int findOrCreateMedicine(String name, String form, String dosage) throws SQLException {
        String find = "SELECT id_medicine FROM medicines WHERE medicine_name=? AND IFNULL(form,'')=? AND IFNULL(dosage,'')=?";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(find)) {
            ps.setString(1, name);
            ps.setString(2, form == null ? "" : form);
            ps.setString(3, dosage == null ? "" : dosage);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        String sql = "INSERT INTO medicines(medicine_name, form, dosage) VALUES(?,?,?)";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, form);
            ps.setString(3, dosage);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public void addMedicineLine(int prescriptionId, int medicineId, String instructions, String duration) throws SQLException {
        String sql = "INSERT INTO prescription_lines(id_prescription, id_medicine, dosage_instructions, duration) VALUES(?,?,?,?)";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, prescriptionId);
            ps.setInt(2, medicineId);
            ps.setString(3, instructions);
            ps.setString(4, duration);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement("DELETE FROM prescriptions WHERE id_prescription=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Object[]> search(String keyword) throws SQLException {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT pr.id_prescription, pr.id_consultation, pr.prescription_date, CONCAT(p.first_name,' ',p.last_name) patient, pr.note " +
                "FROM prescriptions pr JOIN consultations c ON c.id_consultation=pr.id_consultation JOIN patients p ON p.id_patient=c.id_patient " +
                "WHERE p.last_name LIKE ? OR p.first_name LIKE ? OR pr.prescription_date LIKE ? OR pr.note LIKE ? ORDER BY pr.id_prescription DESC";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            String k = "%" + keyword + "%";
            for (int i = 1; i <= 4; i++) ps.setString(i, k);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) rows.add(new Object[]{rs.getInt("id_prescription"), rs.getInt("id_consultation"), rs.getDate("prescription_date").toString(), rs.getString("patient"), rs.getString("note")});
            }
        }
        return rows;
    }

    public String printablePrescription(int id) throws SQLException {
        StringBuilder sb = new StringBuilder();
        String header = "SELECT pr.id_prescription, pr.prescription_date, pr.note, p.last_name, p.first_name, p.birth_date " +
                "FROM prescriptions pr JOIN consultations c ON c.id_consultation=pr.id_consultation JOIN patients p ON p.id_patient=c.id_patient WHERE pr.id_prescription=?";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(header)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    sb.append("MEDICAL CLINIC\n");
                    sb.append("PRESCRIPTION No: ").append(rs.getInt("id_prescription")).append("\n");
                    sb.append("Date: ").append(rs.getDate("prescription_date")).append("\n");
                    sb.append("Patient: ").append(rs.getString("first_name")).append(" ").append(rs.getString("last_name")).append("\n");
                    sb.append("Birth date: ").append(rs.getDate("birth_date")).append("\n\n");
                    sb.append("Medicines:\n");
                }
            }
        }
        String lines = "SELECT m.medicine_name, m.form, m.dosage, l.dosage_instructions, l.duration " +
                "FROM prescription_lines l JOIN medicines m ON m.id_medicine=l.id_medicine WHERE l.id_prescription=?";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(lines)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                int i = 1;
                while (rs.next()) {
                    sb.append(i++).append(". ").append(rs.getString("medicine_name"));
                    sb.append(" - ").append(rs.getString("form"));
                    sb.append(" - ").append(rs.getString("dosage"));
                    sb.append(" | Instructions: ").append(rs.getString("dosage_instructions"));
                    sb.append(" | Duration: ").append(rs.getString("duration")).append("\n");
                }
            }
        }
        sb.append("\nDoctor signature\n");
        return sb.toString();
    }
}
