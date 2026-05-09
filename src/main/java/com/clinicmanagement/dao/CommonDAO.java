package com.clinicmanagement.dao;

import com.clinicmanagement.db.DB;
import com.clinicmanagement.model.Consultation;
import com.clinicmanagement.model.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommonDAO {
    public List<Patient> getPatients() throws SQLException {
        return new PatientDAO().all();
    }

    public List<Consultation> getConsultations() throws SQLException {
        List<Consultation> list = new ArrayList<>();
        String sql = "SELECT * FROM consultations ORDER BY id_consultation DESC";
        try (Connection con = DB.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Consultation c = new Consultation();
                c.id = rs.getInt("id_consultation");
                c.patientId = rs.getInt("id_patient");
                c.doctorId = rs.getInt("id_doctor");
                c.consultationDate = rs.getDate("consultation_date").toString();
                c.symptoms = rs.getString("symptoms");
                c.diagnosis = rs.getString("diagnosis");
                c.treatment = rs.getString("treatment");
                c.report = rs.getString("report");
                list.add(c);
            }
        }
        return list;
    }
}
