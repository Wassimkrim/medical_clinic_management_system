package com.clinicmanagement.ui;

import com.clinicmanagement.dao.UserDAO;
import com.clinicmanagement.util.Ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UsersPanel extends JPanel {
    private final UserDAO dao = new UserDAO();
    private final JTextField id = new JTextField();
    private final JTextField lastName = new JTextField();
    private final JTextField firstName = new JTextField();
    private final JTextField username = new JTextField();
    private final JTextField password = new JTextField();
    private final JComboBox<String> role = new JComboBox<>(new String[]{"ADMIN", "DOCTOR", "SECRETARY"});
    private final JTextField search = new JTextField(18);
    private final DefaultTableModel model = Ui.model("ID", "Last Name", "First Name", "Username", "Role");
    private final JTable table = new JTable(model);

    public UsersPanel() {
        setLayout(new BorderLayout(8, 8));
        build(); load("");
    }

    private void build() {
        id.setEditable(false);
        JPanel form = Ui.form("ID", id, "Last name", lastName, "First name", firstName, "Username", username, "Password", password, "Role", role);
        JButton add = new JButton("Add");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");
        JButton refresh = new JButton("Refresh");
        JButton searchBtn = new JButton("Search");
        JButton clear = new JButton("Clear");
        add.addActionListener(e -> add()); update.addActionListener(e -> update()); delete.addActionListener(e -> delete()); refresh.addActionListener(e -> load("")); searchBtn.addActionListener(e -> load(search.getText())); clear.addActionListener(e -> clear());
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        for (JButton b : new JButton[]{add, update, delete, clear, refresh}) buttons.add(b);
        buttons.add(new JLabel("Search:")); buttons.add(search); buttons.add(searchBtn);
        table.getSelectionModel().addListSelectionListener(e -> select());
        add(form, BorderLayout.WEST); add(Ui.table(table), BorderLayout.CENTER); add(buttons, BorderLayout.SOUTH);
    }

    private void add() { try { dao.insert(lastName.getText(), firstName.getText(), username.getText(), password.getText(), (String) role.getSelectedItem()); Ui.info(this, "User added successfully"); clear(); load(""); } catch (Exception ex) { Ui.error(this, ex); } }
    private void update() { if (id.getText().isEmpty()) { Ui.info(this, "Select a user first"); return; } try { dao.update(Integer.parseInt(id.getText()), lastName.getText(), firstName.getText(), username.getText(), password.getText(), (String) role.getSelectedItem()); Ui.info(this, "User updated successfully"); clear(); load(""); } catch (Exception ex) { Ui.error(this, ex); } }
    private void delete() { if (id.getText().isEmpty()) { Ui.info(this, "Select a user first"); return; } if (!Ui.confirm(this, "Delete this user?")) return; try { dao.delete(Integer.parseInt(id.getText())); Ui.info(this, "User deleted successfully"); clear(); load(""); } catch (Exception ex) { Ui.error(this, ex); } }
    private void load(String key) { try { model.setRowCount(0); for (Object[] row : dao.search(key)) model.addRow(row); } catch (Exception ex) { Ui.error(this, ex); } }
    private void select() {
        int r = table.getSelectedRow(); if (r < 0) return; r = table.convertRowIndexToModel(r);
        id.setText(String.valueOf(model.getValueAt(r, 0))); lastName.setText(String.valueOf(model.getValueAt(r, 1))); firstName.setText(String.valueOf(model.getValueAt(r, 2))); username.setText(String.valueOf(model.getValueAt(r, 3))); role.setSelectedItem(String.valueOf(model.getValueAt(r, 4))); password.setText("");
    }
    private void clear() { id.setText(""); lastName.setText(""); firstName.setText(""); username.setText(""); password.setText(""); role.setSelectedIndex(0); }
}
