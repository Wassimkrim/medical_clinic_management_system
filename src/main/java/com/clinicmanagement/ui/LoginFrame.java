package com.clinicmanagement.ui;

import com.clinicmanagement.dao.AuthDAO;
import com.clinicmanagement.model.User;
import com.clinicmanagement.util.Ui;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private final JTextField username = new JTextField("admin", 20);
    private final JPasswordField password = new JPasswordField("admin123", 20);

    public LoginFrame() {
        setTitle("Login - Medical Clinic");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(430, 250);
        setLocationRelativeTo(null);
        build();
    }

    private void build() {
        JLabel title = new JLabel("Medical Clinic Management", SwingConstants.CENTER);
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        JButton btn = new JButton("Login");
        btn.addActionListener(e -> login());

        JPanel form = Ui.form("Username", username, "Password", password);
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        main.add(title, BorderLayout.NORTH);
        main.add(form, BorderLayout.CENTER);
        main.add(btn, BorderLayout.SOUTH);
        setContentPane(main);
        getRootPane().setDefaultButton(btn);
    }

    private void login() {
        try {
            User u = new AuthDAO().login(username.getText().trim(), new String(password.getPassword()));
            if (u == null) {
                Ui.info(this, "Incorrect username or password");
                return;
            }
            dispose();
            new MainFrame(u).setVisible(true);
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }
}
