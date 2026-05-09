package com.clinicmanagement.ui;

import com.clinicmanagement.dao.CommonDAO;
import com.clinicmanagement.dao.PatientHistoryDAO;
import com.clinicmanagement.model.Patient;
import com.clinicmanagement.util.ComboItem;
import com.clinicmanagement.util.Ui;

import javax.swing.*;
import java.awt.*;

public class PatientHistoryPanel extends JPanel {
    private final JComboBox<ComboItem> patient = new JComboBox<>();
    private final JTextArea output = new JTextArea();

    public PatientHistoryPanel() {
        setLayout(new BorderLayout(8, 8));
        output.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        build(); loadPatients();
    }

    private void build() {
        JButton refresh = new JButton("Refresh Patients");
        JButton show = new JButton("Show History");
        JButton print = new JButton("Print History");
        refresh.addActionListener(e -> loadPatients());
        show.addActionListener(e -> showHistory());
        print.addActionListener(e -> Ui.preview(this, "Patient History", output.getText()));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Patient:")); top.add(patient); top.add(show); top.add(print); top.add(refresh);
        add(top, BorderLayout.NORTH); add(new JScrollPane(output), BorderLayout.CENTER);
    }

    private void loadPatients() {
        try {
            patient.removeAllItems();
            for (Patient p : new CommonDAO().getPatients()) patient.addItem(new ComboItem(p.id, p.firstName + " " + p.lastName));
        } catch (Exception ex) { Ui.error(this, ex); }
    }

    private void showHistory() {
        try {
            ComboItem item = (ComboItem) patient.getSelectedItem();
            if (item == null) return;
            output.setText(new PatientHistoryDAO().history(item.id));
        } catch (Exception ex) { Ui.error(this, ex); }
    }
}
