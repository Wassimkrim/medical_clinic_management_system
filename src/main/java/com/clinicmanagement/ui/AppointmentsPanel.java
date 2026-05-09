package com.clinicmanagement.ui;

import com.clinicmanagement.dao.AppointmentDAO;
import com.clinicmanagement.dao.CommonDAO;
import com.clinicmanagement.model.Patient;
import com.clinicmanagement.model.User;
import com.clinicmanagement.util.ComboItem;
import com.clinicmanagement.util.Ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AppointmentsPanel extends JPanel {
    private final AppointmentDAO dao = new AppointmentDAO();
    private final User user;
    private final JTextField id = new JTextField();
    private final JComboBox<ComboItem> patient = new JComboBox<>();
    private final JTextField date = new JTextField();
    private final JTextField time = new JTextField();
    private final JTextField reason = new JTextField();
    private final JComboBox<String> status = new JComboBox<>(new String[]{"Planned", "Cancelled", "Completed"});
    private final JTextField search = new JTextField(18);
    private final DefaultTableModel model = Ui.model("ID", "Patient ID", "Patient", "Date", "Time", "Reason", "Status");
    private final JTable table = new JTable(model);

    public AppointmentsPanel(User user) {
        this.user = user;
        setLayout(new BorderLayout(8, 8));
        build(); loadPatients(); load("");
    }

    private void build() {
        id.setEditable(false);
        JPanel form = Ui.form("ID", id, "Patient", patient, "Appointment date yyyy-mm-dd", date, "Time hh:mm:ss", time, "Reason", reason, "Status", status);
        JButton add = new JButton("Add Appointment");
        JButton update = new JButton("Update Appointment");
        JButton cancel = new JButton("Cancel Appointment");
        JButton delete = new JButton("Delete");
        JButton refresh = new JButton("Refresh");
        JButton searchBtn = new JButton("Search");
        JButton clear = new JButton("Clear");
        add.addActionListener(e -> add());
        update.addActionListener(e -> update());
        cancel.addActionListener(e -> { status.setSelectedItem("Cancelled"); update(); });
        delete.addActionListener(e -> delete());
        refresh.addActionListener(e -> { loadPatients(); load(""); });
        searchBtn.addActionListener(e -> load(search.getText()));
        clear.addActionListener(e -> clear());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (JButton b : new JButton[]{add, update, cancel, delete, clear, refresh}) buttons.add(b);
        buttons.add(new JLabel("Search:")); buttons.add(search); buttons.add(searchBtn);
        table.getSelectionModel().addListSelectionListener(e -> select());
        add(form, BorderLayout.WEST); add(Ui.table(table), BorderLayout.CENTER); add(buttons, BorderLayout.SOUTH);
    }

    private void loadPatients() {
        try {
            patient.removeAllItems();
            for (Patient p : new CommonDAO().getPatients()) patient.addItem(new ComboItem(p.id, p.firstName + " " + p.lastName));
        } catch (Exception ex) { Ui.error(this, ex); }
    }

    private int selectedPatientId() { return ((ComboItem) patient.getSelectedItem()).id; }

    private void add() {
        try { dao.insert(selectedPatientId(), user.id, date.getText(), time.getText(), reason.getText(), (String) status.getSelectedItem()); Ui.info(this, "Appointment added successfully"); clear(); load(""); }
        catch (Exception ex) { Ui.error(this, ex); }
    }

    private void update() {
        if (id.getText().isEmpty()) { Ui.info(this, "Select an appointment first"); return; }
        try { dao.update(Integer.parseInt(id.getText()), selectedPatientId(), date.getText(), time.getText(), reason.getText(), (String) status.getSelectedItem()); Ui.info(this, "Appointment updated successfully"); clear(); load(""); }
        catch (Exception ex) { Ui.error(this, ex); }
    }

    private void delete() {
        if (id.getText().isEmpty()) { Ui.info(this, "Select an appointment first"); return; }
        if (!Ui.confirm(this, "Delete this appointment?")) return;
        try { dao.delete(Integer.parseInt(id.getText())); Ui.info(this, "Appointment deleted successfully"); clear(); load(""); }
        catch (Exception ex) { Ui.error(this, ex); }
    }

    private void load(String key) {
        try { model.setRowCount(0); for (Object[] row : dao.search(key)) model.addRow(row); }
        catch (Exception ex) { Ui.error(this, ex); }
    }

    private void select() {
        int r = table.getSelectedRow(); if (r < 0) return; r = table.convertRowIndexToModel(r);
        id.setText(String.valueOf(model.getValueAt(r, 0)));
        int pid = Integer.parseInt(String.valueOf(model.getValueAt(r, 1)));
        for (int i = 0; i < patient.getItemCount(); i++) if (patient.getItemAt(i).id == pid) patient.setSelectedIndex(i);
        date.setText(String.valueOf(model.getValueAt(r, 3))); time.setText(String.valueOf(model.getValueAt(r, 4))); reason.setText(String.valueOf(model.getValueAt(r, 5))); status.setSelectedItem(String.valueOf(model.getValueAt(r, 6)));
    }

    private void clear() { id.setText(""); date.setText(""); time.setText(""); reason.setText(""); status.setSelectedIndex(0); }
}
