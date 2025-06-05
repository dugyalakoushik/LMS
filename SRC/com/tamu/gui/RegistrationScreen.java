package com.tamu.gui;
import javax.swing.*;

import com.google.gson.Gson;
import com.tamu.entity.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class RegistrationScreen extends JFrame implements ActionListener {
    private JTextField txtUsername = new JTextField(20);
    private JPasswordField txtPassword = new JPasswordField(20);
    private JTextField txtFirstName = new JTextField(20);
    private JTextField txtLastName = new JTextField(20);
    private JTextField txtAge = new JTextField(20);
    private JTextField txtActivity = new JTextField(20);
    private JTextField txtEmail = new JTextField(20);
    private JButton btnRegister = new JButton("Register");
    Gson gson = new Gson();

    public RegistrationScreen() {
        this.setTitle("User Registration");
        this.setSize(400, 300);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainPanel.add(createLabelAndField("Username:", txtUsername));
        mainPanel.add(createLabelAndField("Password:", txtPassword));
        mainPanel.add(createLabelAndField("First Name:", txtFirstName));
        mainPanel.add(createLabelAndField("Last Name:", txtLastName));
        mainPanel.add(createLabelAndField("Age:", txtAge));
        mainPanel.add(createLabelAndField("Activity:", txtActivity));
        mainPanel.add(createLabelAndField("Email:", txtEmail));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnRegister);

        this.add(mainPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);

        btnRegister.addActionListener(this);
    }

    private JPanel createLabelAndField(String labelText, JTextField textField) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(100, 20));  // Set a fixed width for labels
        panel.add(label);
        panel.add(textField);
        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRegister) {
        	User newUser = new User();
            newUser.setUsername(txtUsername.getText().trim());
            newUser.setPassword(txtPassword.getText().trim());
            newUser.setFirstName(txtFirstName.getText().trim());
            newUser.setLastName(txtLastName.getText().trim());
            newUser.setAge(Integer.parseInt((txtAge.getText().trim())));;
            newUser.setActivity(txtActivity.getText().trim());
            newUser.setEmail(txtEmail.getText().trim());
            String userBody = gson.toJson(newUser);
            try {
                String registeredUser = Application.getInstance().getDataAdapter().register(userBody);
                if (registeredUser == "User Registered Successfully") {
                    JOptionPane.showMessageDialog(null, "Registration failed. Please try again.");
                } else {
                    JOptionPane.showMessageDialog(null, "Registration successful!");
                    this.setVisible(false);
                    LoginScreen loginScreen = new LoginScreen();
                    loginScreen.setVisible(true);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            this.setVisible(false);
            LoginScreen loginScreen = new LoginScreen();
            loginScreen.setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegistrationScreen().setVisible(true));
    }
}
