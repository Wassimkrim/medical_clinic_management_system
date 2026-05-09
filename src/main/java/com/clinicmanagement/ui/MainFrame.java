package com.clinicmanagement.ui;

import com.clinicmanagement.model.User;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private final User user;

    public MainFrame(User user) {
        this.user = user;
        setTitle("Medical Clinic - " + user.getFullName() + " (" + user.role + ")");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1120, 740);
        setLocationRelativeTo(null);
        build();
    }

    private void build() {
        JTabbedPane tabs = new JTabbedPane();
        String role = user.role;

        if (role.equals("ADMIN") || role.equals("SECRETARY") || role.equals("DOCTOR")) {
            tabs.addTab("Patients", new PatientsPanel());
        }
        if (role.equals("ADMIN") || role.equals("SECRETARY")) {
            tabs.addTab("Appointments", new AppointmentsPanel(user));
        }
        if (role.equals("ADMIN") || role.equals("DOCTOR")) {
            tabs.addTab("Consultations", new ConsultationsPanel(user));
            tabs.addTab("Prescriptions", new PrescriptionsPanel());
            tabs.addTab("Certificates", new CertificatesPanel());
            tabs.addTab("Medical Tests", new MedicalTestsPanel());
        }
        tabs.addTab("Patient History", new PatientHistoryPanel());
        if (role.equals("ADMIN")) {
            tabs.addTab("Users", new UsersPanel());
        }

        JLabel header = new JLabel("  Medical clinic management software", SwingConstants.LEFT);
        header.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        header.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        JPanel top = new JPanel(new BorderLayout());
        top.add(header, BorderLayout.CENTER);
        top.add(logout, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }
}
