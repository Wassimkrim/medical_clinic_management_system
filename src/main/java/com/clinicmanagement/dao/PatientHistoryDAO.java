package com.clinicmanagement.dao;

import com.clinicmanagement.db.DB;
import java.sql.*;

public class PatientHistoryDAO {
    public String history(int patientId) throws SQLException {
        StringBuilder sb = new StringBuilder();
        String patientSql = "SELECT * FROM patients WHERE id_patient=?";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(patientSql)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    sb.append("PATIENT MEDICAL HISTORY\n");
                    sb.append("Patient: ").append(rs.getString("first_name")).append(" ").append(rs.getString("last_name")).append("\n");
                    sb.append("Phone: ").append(rs.getString("phone")).append("\n");
                    sb.append("Blood group: ").append(rs.getString("blood_group")).append("\n");
                    sb.append("Allergies: ").append(rs.getString("allergies")).append("\n");
                    sb.append("Chronic diseases: ").append(rs.getString("chronic_diseases")).append("\n\n");
                }
            }
        }

        sb.append("CONSULTATIONS:\n");
        String cSql = "SELECT * FROM consultations WHERE id_patient=? ORDER BY consultation_date DESC";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(cSql)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sb.append("- ").append(rs.getDate("consultation_date")).append(" | Diagnosis: ").append(rs.getString("diagnosis")).append(" | Treatment: ").append(rs.getString("treatment")).append("\n");
                    sb.append("  Report: ").append(rs.getString("report")).append("\n");
                }
            }
        }

        sb.append("\nPRESCRIPTIONS:\n");
        String pSql = "SELECT pr.* FROM prescriptions pr JOIN consultations c ON c.id_consultation=pr.id_consultation WHERE c.id_patient=? ORDER BY pr.prescription_date DESC";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(pSql)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sb.append("- Prescription No ").append(rs.getInt("id_prescription")).append(" on ").append(rs.getDate("prescription_date")).append(" | ").append(rs.getString("note")).append("\n");
                }
            }
        }

        sb.append("\nCERTIFICATES:\n");
        String ceSql = "SELECT ce.* FROM certificates ce JOIN consultations c ON c.id_consultation=ce.id_consultation WHERE c.id_patient=? ORDER BY ce.start_date DESC";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(ceSql)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sb.append("- ").append(rs.getString("certificate_type")).append(" on ").append(rs.getDate("start_date")).append(" | ").append(rs.getInt("duration_days")).append(" days | ").append(rs.getString("description")).append("\n");
                }
            }
        }

        sb.append("\nMEDICAL TESTS:\n");
        String tSql = "SELECT mt.* FROM medical_tests mt JOIN consultations c ON c.id_consultation=mt.id_consultation WHERE c.id_patient=? ORDER BY mt.test_date DESC";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(tSql)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sb.append("- ").append(rs.getString("test_type")).append(" on ").append(rs.getDate("test_date")).append(" | ").append(rs.getString("result")).append("\n");
                }
            }
        }
        return sb.toString();
    }
}
