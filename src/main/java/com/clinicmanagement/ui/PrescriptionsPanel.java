package com.clinicmanagement.ui;

import com.clinicmanagement.dao.CommonDAO;
import com.clinicmanagement.dao.PrescriptionDAO;
import com.clinicmanagement.model.Consultation;
import com.clinicmanagement.util.ComboItem;
import com.clinicmanagement.util.Ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PrescriptionsPanel extends JPanel {
    private final PrescriptionDAO dao = new PrescriptionDAO();
    private final JTextField idPrescription = new JTextField();
    private final JComboBox<ComboItem> consultation = new JComboBox<>();
    private final JTextField date = new JTextField();
    private final JTextField note = new JTextField();
    private final JTextField medName = new JTextField();
    private final JTextField medForm = new JTextField();
    private final JTextField medDosage = new JTextField();
    private final JTextField instructions = new JTextField();
    private final JTextField duration = new JTextField();
    private final JTextField search = new JTextField(18);
    private final DefaultTableModel model = Ui.model("Prescription ID", "Consultation ID", "Date", "Patient", "Note");
    private final JTable table = new JTable(model);

    public PrescriptionsPanel() {
        setLayout(new BorderLayout(8, 8));
        build(); loadConsultations(); load("");
    }

    private void build() {
        idPrescription.setEditable(false);
        JPanel form = Ui.form("Prescription ID", idPrescription, "Consultation", consultation, "Prescription date yyyy-mm-dd", date, "Note", note, "Medicine", medName, "Form", medForm, "Dosage", medDosage, "Instructions", instructions, "Duration", duration);
        JButton create = new JButton("Create Prescription");
        JButton update = new JButton("Update Prescription");
        JButton addMed = new JButton("Add Medicine");
        JButton delete = new JButton("Delete Prescription");
        JButton print = new JButton("Print Prescription");
        JButton refresh = new JButton("Refresh");
        JButton searchBtn = new JButton("Search");
        JButton clear = new JButton("Clear");
        create.addActionListener(e -> create()); update.addActionListener(e -> update()); addMed.addActionListener(e -> addMedicine()); delete.addActionListener(e -> delete()); print.addActionListener(e -> print()); refresh.addActionListener(e -> { loadConsultations(); load(""); }); searchBtn.addActionListener(e -> load(search.getText())); clear.addActionListener(e -> clear());
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (JButton b : new JButton[]{create, update, addMed, delete, print, clear, refresh}) buttons.add(b);
        buttons.add(new JLabel("Search:")); buttons.add(search); buttons.add(searchBtn);
        table.getSelectionModel().addListSelectionListener(e -> select());
        add(form, BorderLayout.WEST); add(Ui.table(table), BorderLayout.CENTER); add(buttons, BorderLayout.SOUTH);
    }

    private void loadConsultations() { try { consultation.removeAllItems(); for (Consultation c : new CommonDAO().getConsultations()) consultation.addItem(new ComboItem(c.id, "Consultation " + c.id + " - " + c.consultationDate)); } catch (Exception ex) { Ui.error(this, ex); } }
    private int selectedConsultationId() { return ((ComboItem) consultation.getSelectedItem()).id; }
    private void create() { try { int newId = dao.createPrescription(selectedConsultationId(), date.getText(), note.getText()); idPrescription.setText(String.valueOf(newId)); Ui.info(this, "Prescription created. You can now add medicines."); load(""); } catch (Exception ex) { Ui.error(this, ex); } }
    private void update() { if (idPrescription.getText().isEmpty()) { Ui.info(this, "Select a prescription first"); return; } try { dao.updatePrescription(Integer.parseInt(idPrescription.getText()), selectedConsultationId(), date.getText(), note.getText()); Ui.info(this, "Prescription updated successfully"); load(""); } catch (Exception ex) { Ui.error(this, ex); } }
    private void addMedicine() { if (idPrescription.getText().isEmpty()) { Ui.info(this, "Create or select a prescription first"); return; } try { int medId = dao.findOrCreateMedicine(medName.getText(), medForm.getText(), medDosage.getText()); dao.addMedicineLine(Integer.parseInt(idPrescription.getText()), medId, instructions.getText(), duration.getText()); Ui.info(this, "Medicine added to prescription"); medName.setText(""); medForm.setText(""); medDosage.setText(""); instructions.setText(""); duration.setText(""); } catch (Exception ex) { Ui.error(this, ex); } }
    private void delete() { if (idPrescription.getText().isEmpty()) { Ui.info(this, "Select a prescription first"); return; } if (!Ui.confirm(this, "Delete this prescription?")) return; try { dao.delete(Integer.parseInt(idPrescription.getText())); Ui.info(this, "Prescription deleted successfully"); clear(); load(""); } catch (Exception ex) { Ui.error(this, ex); } }
    private void print() { if (idPrescription.getText().isEmpty()) { Ui.info(this, "Select a prescription first"); return; } try { Ui.preview(this, "Prescription", dao.printablePrescription(Integer.parseInt(idPrescription.getText()))); } catch (Exception ex) { Ui.error(this, ex); } }
    private void load(String key) { try { model.setRowCount(0); for (Object[] row : dao.search(key)) model.addRow(row); } catch (Exception ex) { Ui.error(this, ex); } }

    private void select() {
        int r = table.getSelectedRow(); if (r < 0) return; r = table.convertRowIndexToModel(r);
        idPrescription.setText(String.valueOf(model.getValueAt(r, 0)));
        int cid = Integer.parseInt(String.valueOf(model.getValueAt(r, 1)));
        for (int i = 0; i < consultation.getItemCount(); i++) if (consultation.getItemAt(i).id == cid) consultation.setSelectedIndex(i);
        date.setText(String.valueOf(model.getValueAt(r, 2))); note.setText(String.valueOf(model.getValueAt(r, 4)));
    }
    private void clear() { idPrescription.setText(""); date.setText(""); note.setText(""); medName.setText(""); medForm.setText(""); medDosage.setText(""); instructions.setText(""); duration.setText(""); }
}
