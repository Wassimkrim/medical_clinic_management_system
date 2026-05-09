package com.clinicmanagement.ui;

import com.clinicmanagement.dao.CommonDAO;
import com.clinicmanagement.dao.ConsultationDAO;
import com.clinicmanagement.model.Patient;
import com.clinicmanagement.model.User;
import com.clinicmanagement.util.ComboItem;
import com.clinicmanagement.util.Ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ConsultationsPanel extends JPanel {
    private final ConsultationDAO dao = new ConsultationDAO();
    private final User doctor;
    private final JTextField id = new JTextField();
    private final JComboBox<ComboItem> patient = new JComboBox<>();
    private final JTextField date = new JTextField();
    private final JTextArea symptoms = new JTextArea(3, 20);
    private final JTextArea diagnosis = new JTextArea(3, 20);
    private final JTextArea treatment = new JTextArea(3, 20);
    private final JTextArea report = new JTextArea(3, 20);
    private final JTextField search = new JTextField(18);
    private final DefaultTableModel model = Ui.model("ID", "Patient ID", "Patient", "Date", "Symptoms", "Diagnosis", "Treatment", "Report");
    private final JTable table = new JTable(model);

    public ConsultationsPanel(User doctor) {
        this.doctor = doctor;
        setLayout(new BorderLayout(8, 8));
        build(); loadPatients(); load("");
    }

    private void build() {
        id.setEditable(false);
        JPanel form = Ui.form("ID", id, "Patient", patient, "Consultation date yyyy-mm-dd", date, "Symptoms", new JScrollPane(symptoms), "Diagnosis", new JScrollPane(diagnosis), "Treatment", new JScrollPane(treatment), "Medical report", new JScrollPane(report));
        JButton add = new JButton("Add Consultation");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");
        JButton refresh = new JButton("Refresh");
        JButton searchBtn = new JButton("Search");
        JButton clear = new JButton("Clear");
        add.addActionListener(e -> add()); update.addActionListener(e -> update()); delete.addActionListener(e -> delete()); refresh.addActionListener(e -> { loadPatients(); load(""); }); searchBtn.addActionListener(e -> load(search.getText())); clear.addActionListener(e -> clear());
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (JButton b : new JButton[]{add, update, delete, clear, refresh}) buttons.add(b);
        buttons.add(new JLabel("Search:")); buttons.add(search); buttons.add(searchBtn);
        table.getSelectionModel().addListSelectionListener(e -> select());
        add(form, BorderLayout.WEST); add(Ui.table(table), BorderLayout.CENTER); add(buttons, BorderLayout.SOUTH);
    }

    private void loadPatients() { try { patient.removeAllItems(); for (Patient p : new CommonDAO().getPatients()) patient.addItem(new ComboItem(p.id, p.firstName + " " + p.lastName)); } catch (Exception ex) { Ui.error(this, ex); } }
    private int selectedPatientId() { return ((ComboItem) patient.getSelectedItem()).id; }

    private void add() { try { dao.insert(selectedPatientId(), doctor.id, date.getText(), symptoms.getText(), diagnosis.getText(), treatment.getText(), report.getText()); Ui.info(this, "Consultation added successfully"); clear(); load(""); } catch (Exception ex) { Ui.error(this, ex); } }
    private void update() { if (id.getText().isEmpty()) { Ui.info(this, "Select a consultation first"); return; } try { dao.update(Integer.parseInt(id.getText()), selectedPatientId(), date.getText(), symptoms.getText(), diagnosis.getText(), treatment.getText(), report.getText()); Ui.info(this, "Consultation updated successfully"); clear(); load(""); } catch (Exception ex) { Ui.error(this, ex); } }
    private void delete() { if (id.getText().isEmpty()) { Ui.info(this, "Select a consultation first"); return; } if (!Ui.confirm(this, "Delete this consultation?")) return; try { dao.delete(Integer.parseInt(id.getText())); Ui.info(this, "Consultation deleted successfully"); clear(); load(""); } catch (Exception ex) { Ui.error(this, ex); } }
    private void load(String key) { try { model.setRowCount(0); for (Object[] row : dao.search(key)) model.addRow(row); } catch (Exception ex) { Ui.error(this, ex); } }

    private void select() {
        int r = table.getSelectedRow(); if (r < 0) return; r = table.convertRowIndexToModel(r);
        id.setText(String.valueOf(model.getValueAt(r, 0)));
        int pid = Integer.parseInt(String.valueOf(model.getValueAt(r, 1)));
        for (int i = 0; i < patient.getItemCount(); i++) if (patient.getItemAt(i).id == pid) patient.setSelectedIndex(i);
        date.setText(String.valueOf(model.getValueAt(r, 3))); symptoms.setText(String.valueOf(model.getValueAt(r, 4))); diagnosis.setText(String.valueOf(model.getValueAt(r, 5))); treatment.setText(String.valueOf(model.getValueAt(r, 6))); report.setText(String.valueOf(model.getValueAt(r, 7)));
    }
    private void clear() { id.setText(""); date.setText(""); symptoms.setText(""); diagnosis.setText(""); treatment.setText(""); report.setText(""); }
}
