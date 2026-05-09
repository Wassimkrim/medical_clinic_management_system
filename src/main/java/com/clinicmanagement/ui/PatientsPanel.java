package com.clinicmanagement.ui;

import com.clinicmanagement.dao.PatientDAO;
import com.clinicmanagement.model.Patient;
import com.clinicmanagement.util.Ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PatientsPanel extends JPanel {
    private final PatientDAO dao = new PatientDAO();
    private final JTextField id = new JTextField();
    private final JTextField lastName = new JTextField();
    private final JTextField firstName = new JTextField();
    private final JTextField birthDate = new JTextField();
    private final JComboBox<String> gender = new JComboBox<>(new String[]{"Male", "Female"});
    private final JTextField phone = new JTextField();
    private final JTextField address = new JTextField();
    private final JTextField bloodGroup = new JTextField();
    private final JTextField allergies = new JTextField();
    private final JTextField chronicDiseases = new JTextField();
    private final JTextField search = new JTextField(18);
    private final DefaultTableModel model = Ui.model("ID", "Last Name", "First Name", "Birth Date", "Gender", "Phone", "Address", "Blood", "Allergies", "Chronic Diseases");
    private final JTable table = new JTable(model);

    public PatientsPanel() {
        setLayout(new BorderLayout(8, 8));
        build();
        load("");
    }

    private void build() {
        id.setEditable(false);
        JPanel form = Ui.form(
                "ID", id,
                "Last name", lastName,
                "First name", firstName,
                "Birth date yyyy-mm-dd", birthDate,
                "Gender", gender,
                "Phone", phone,
                "Address", address,
                "Blood group", bloodGroup,
                "Allergies", allergies,
                "Chronic diseases", chronicDiseases
        );
        JButton add = new JButton("Add");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");
        JButton clear = new JButton("Clear");
        JButton refresh = new JButton("Refresh");
        JButton searchBtn = new JButton("Search");
        add.addActionListener(e -> add());
        update.addActionListener(e -> update());
        delete.addActionListener(e -> delete());
        clear.addActionListener(e -> clear());
        refresh.addActionListener(e -> load(""));
        searchBtn.addActionListener(e -> load(search.getText()));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (JButton b : new JButton[]{add, update, delete, clear, refresh}) buttons.add(b);
        buttons.add(new JLabel("Search:")); buttons.add(search); buttons.add(searchBtn);

        table.getSelectionModel().addListSelectionListener(e -> select());
        add(form, BorderLayout.WEST);
        add(Ui.table(table), BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
    }

    private Patient getPatient() {
        Patient p = new Patient();
        if (!id.getText().isEmpty()) p.id = Integer.parseInt(id.getText());
        p.lastName = lastName.getText();
        p.firstName = firstName.getText();
        p.birthDate = birthDate.getText();
        p.gender = (String) gender.getSelectedItem();
        p.phone = phone.getText();
        p.address = address.getText();
        p.bloodGroup = bloodGroup.getText();
        p.allergies = allergies.getText();
        p.chronicDiseases = chronicDiseases.getText();
        return p;
    }

    private void add() {
        try { dao.insert(getPatient()); Ui.info(this, "Patient added successfully"); clear(); load(""); }
        catch (Exception ex) { Ui.error(this, ex); }
    }

    private void update() {
        if (id.getText().isEmpty()) { Ui.info(this, "Select a patient first"); return; }
        try { dao.update(getPatient()); Ui.info(this, "Patient updated successfully"); clear(); load(""); }
        catch (Exception ex) { Ui.error(this, ex); }
    }

    private void delete() {
        if (id.getText().isEmpty()) { Ui.info(this, "Select a patient first"); return; }
        if (!Ui.confirm(this, "Delete this patient?")) return;
        try { dao.delete(Integer.parseInt(id.getText())); Ui.info(this, "Patient deleted successfully"); clear(); load(""); }
        catch (Exception ex) { Ui.error(this, ex); }
    }

    private void load(String key) {
        try {
            model.setRowCount(0);
            List<Patient> patients = dao.search(key);
            for (Patient p : patients) model.addRow(new Object[]{p.id, p.lastName, p.firstName, p.birthDate, p.gender, p.phone, p.address, p.bloodGroup, p.allergies, p.chronicDiseases});
        } catch (Exception ex) { Ui.error(this, ex); }
    }

    private void select() {
        int r = table.getSelectedRow();
        if (r < 0) return;
        r = table.convertRowIndexToModel(r);
        id.setText(String.valueOf(model.getValueAt(r, 0)));
        lastName.setText(String.valueOf(model.getValueAt(r, 1)));
        firstName.setText(String.valueOf(model.getValueAt(r, 2)));
        birthDate.setText(String.valueOf(model.getValueAt(r, 3)));
        gender.setSelectedItem(String.valueOf(model.getValueAt(r, 4)));
        phone.setText(String.valueOf(model.getValueAt(r, 5)));
        address.setText(String.valueOf(model.getValueAt(r, 6)));
        bloodGroup.setText(String.valueOf(model.getValueAt(r, 7)));
        allergies.setText(String.valueOf(model.getValueAt(r, 8)));
        chronicDiseases.setText(String.valueOf(model.getValueAt(r, 9)));
    }

    private void clear() {
        id.setText(""); lastName.setText(""); firstName.setText(""); birthDate.setText(""); phone.setText(""); address.setText("");
        bloodGroup.setText(""); allergies.setText(""); chronicDiseases.setText(""); gender.setSelectedIndex(0);
    }
}
