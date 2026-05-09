package com.clinicmanagement.util;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.print.PrinterException;

public class Ui {
    public static JPanel form(Object... pairs) {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        for (int i = 0; i < pairs.length; i += 2) {
            c.gridx = 0;
            c.weightx = 0;
            p.add(new JLabel(String.valueOf(pairs[i]) + ":"), c);
            c.gridx = 1;
            c.weightx = 1;
            Component comp = (Component) pairs[i + 1];
            p.add(comp, c);
            c.gridy++;
        }
        return p;
    }

    public static DefaultTableModel model(String... cols) {
        return new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    public static JScrollPane table(JTable t) {
        t.setAutoCreateRowSorter(true);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return new JScrollPane(t);
    }

    public static void info(Component c, String msg) {
        JOptionPane.showMessageDialog(c, msg, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void error(Component c, Exception e) {
        JOptionPane.showMessageDialog(c, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static boolean confirm(Component c, String msg) {
        return JOptionPane.showConfirmDialog(c, msg, "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    public static void printText(Component parent, JTextArea area) {
        try {
            area.print();
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(parent, "Printing error: " + e.getMessage());
        }
    }

    public static void preview(Component parent, String title, String content) {
        JTextArea area = new JTextArea(content, 25, 70);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        area.setEditable(false);
        int option = JOptionPane.showConfirmDialog(parent, new JScrollPane(area), title + " - Print?", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
            printText(parent, area);
        }
    }
}
