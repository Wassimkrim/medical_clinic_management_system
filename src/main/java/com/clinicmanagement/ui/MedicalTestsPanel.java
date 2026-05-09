package com.clinicmanagement.ui;

import com.clinicmanagement.dao.CommonDAO;
import com.clinicmanagement.dao.MedicalTestDAO;
import com.clinicmanagement.model.Consultation;
import com.clinicmanagement.util.ComboItem;
import com.clinicmanagement.util.Ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MedicalTestsPanel extends JPanel {
    private final MedicalTestDAO dao = new MedicalTestDAO();
    private final JTextField id = new JTextField();
    private final JComboBox<ComboItem> consultation = new JComboBox<>();
    private final JTextField testType = new JTextField();
    private final JTextField testDate = new JTextField();
    private final JTextArea result = new JTextArea(5, 20);
    private final JTextField search = new JTextField(18);
    private final DefaultTableModel model = Ui.model("ID", "Consultation ID", "Test Type", "Date", "Patient", "Result");
    private final JTable table = new JTable(model);

    public MedicalTestsPanel() {
        setLayout(new BorderLayout(8, 8));
        build(); loadConsultations(); load("");
    }

    private void build() {
        id.setEditable(false);
        JPanel form = Ui.form("ID", id, "Consultation", consultation, "Test type", testType, "Test date yyyy-mm-dd", testDate, "Result", new JScrollPane(result));
        JButton add = new JButton("Add Medical Test");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");
        JButton print = new JButton("Print");
        JButton refresh = new JButton("Refresh");
        JButton searchBtn = new JButton("Search");
        JButton clear = new JButton("Clear");
        add.addActionListener(e -> add()); update.addActionListener(e -> update()); delete.addActionListener(e -> delete()); print.addActionListener(e -> print()); refresh.addActionListener(e -> { loadConsultations(); load(""); }); searchBtn.addActionListener(e -> load(search.getText())); clear.addActionListener(e -> clear());
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (JButton b : new JButton[]{add, update, delete, print, clear, refresh}) buttons.add(b);
        buttons.add(new JLabel("Search:")); buttons.add(search); buttons.add(searchBtn);
        table.getSelectionModel().addListSelectionListener(e -> select());
        add(form, BorderLayout.WEST); add(Ui.table(table), BorderLayout.CENTER); add(buttons, BorderLayout.SOUTH);
    }

    private void loadConsultations() { try { consultation.removeAllItems(); for (Consultation c : new CommonDAO().getConsultations()) consultation.addItem(new ComboItem(c.id, "Consultation " + c.id + " - " + c.consultationDate)); } catch (Exception ex) { Ui.error(this, ex); } }
    private int selectedConsultationId() { return ((ComboItem) consultation.getSelectedItem()).id; }
    private void add() { try { dao.insert(selectedConsultationId(), testType.getText(), testDate.getText(), result.getText()); Ui.info(this, "Medical test added successfully"); clear(); load(""); } catch (Exception ex) { Ui.error(this, ex); } }
    private void update() { if (id.getText().isEmpty()) { Ui.info(this, "Select a medical test first"); return; } try { dao.update(Integer.parseInt(id.getText()), selectedConsultationId(), testType.getText(), testDate.getText(), result.getText()); Ui.info(this, "Medical test updated successfully"); clear(); load(""); } catch (Exception ex) { Ui.error(this, ex); } }
    private void delete() { if (id.getText().isEmpty()) { Ui.info(this, "Select a medical test first"); return; } if (!Ui.confirm(this, "Delete this medical test?")) return; try { dao.delete(Integer.parseInt(id.getText())); Ui.info(this, "Medical test deleted successfully"); clear(); load(""); } catch (Exception ex) { Ui.error(this, ex); } }
    private void print() { if (id.getText().isEmpty()) { Ui.info(this, "Select a medical test first"); return; } try { Ui.preview(this, "Medical Test", dao.printableTest(Integer.parseInt(id.getText()))); } catch (Exception ex) { Ui.error(this, ex); } }
    private void load(String key) { try { model.setRowCount(0); for (Object[] row : dao.search(key)) model.addRow(row); } catch (Exception ex) { Ui.error(this, ex); } }

    private void select() {
        int r = table.getSelectedRow(); if (r < 0) return; r = table.convertRowIndexToModel(r);
        id.setText(String.valueOf(model.getValueAt(r, 0)));
        int cid = Integer.parseInt(String.valueOf(model.getValueAt(r, 1)));
        for (int i = 0; i < consultation.getItemCount(); i++) if (consultation.getItemAt(i).id == cid) consultation.setSelectedIndex(i);
        testType.setText(String.valueOf(model.getValueAt(r, 2))); testDate.setText(String.valueOf(model.getValueAt(r, 3))); result.setText(String.valueOf(model.getValueAt(r, 5)));
    }
    private void clear() { id.setText(""); testType.setText(""); testDate.setText(""); result.setText(""); }
}
