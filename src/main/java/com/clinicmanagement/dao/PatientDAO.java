package com.clinicmanagement.dao;

import com.clinicmanagement.db.DB;
import com.clinicmanagement.model.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO {
    public void insert(Patient p) throws SQLException {
        String sql = "INSERT INTO patients(last_name, first_name, birth_date, gender, phone, address, blood_group, allergies, chronic_diseases) VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            fill(ps, p, false);
            ps.executeUpdate();
        }
    }

    public void update(Patient p) throws SQLException {
        String sql = "UPDATE patients SET last_name=?, first_name=?, birth_date=?, gender=?, phone=?, address=?, blood_group=?, allergies=?, chronic_diseases=? WHERE id_patient=?";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            fill(ps, p, true);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement("DELETE FROM patients WHERE id_patient=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public List<Patient> search(String keyword) throws SQLException {
        List<Patient> list = new ArrayList<>();
        String sql = "SELECT * FROM patients WHERE last_name LIKE ? OR first_name LIKE ? OR phone LIKE ? OR CAST(id_patient AS CHAR) LIKE ? ORDER BY id_patient DESC";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            String k = "%" + keyword + "%";
            for (int i = 1; i <= 4; i++) ps.setString(i, k);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(map(rs));
            }
        }
        return list;
    }

    public List<Patient> all() throws SQLException {
        return search("");
    }

    public Patient findById(int id) throws SQLException {
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement("SELECT * FROM patients WHERE id_patient=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    private void fill(PreparedStatement ps, Patient p, boolean includeId) throws SQLException {
        ps.setString(1, p.lastName);
        ps.setString(2, p.firstName);
        ps.setString(3, p.birthDate);
        ps.setString(4, p.gender);
        ps.setString(5, p.phone);
        ps.setString(6, p.address);
        ps.setString(7, p.bloodGroup);
        ps.setString(8, p.allergies);
        ps.setString(9, p.chronicDiseases);
        if (includeId) ps.setInt(10, p.id);
    }

    private Patient map(ResultSet rs) throws SQLException {
        Patient p = new Patient();
        p.id = rs.getInt("id_patient");
        p.lastName = rs.getString("last_name");
        p.firstName = rs.getString("first_name");
        Date d = rs.getDate("birth_date");
        p.birthDate = d == null ? "" : d.toString();
        p.gender = rs.getString("gender");
        p.phone = rs.getString("phone");
        p.address = rs.getString("address");
        p.bloodGroup = rs.getString("blood_group");
        p.allergies = rs.getString("allergies");
        p.chronicDiseases = rs.getString("chronic_diseases");
        return p;
    }
}
