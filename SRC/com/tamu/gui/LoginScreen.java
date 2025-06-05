package com.tamu.gui;
import javax.swing.*;

import com.google.gson.Gson;
import com.tamu.entity.User;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LoginScreen extends JFrame implements ActionListener {
    private JTextField txtUserName = new JTextField(10);
    private JPasswordField txtPassword = new JPasswordField(10);
    private JButton btnLogin = new JButton("Login");
    private JButton btnRegister = new JButton("Register");
    Gson gson = new Gson();

    public JButton getBtnLogin() {
        return btnLogin;
    }

    public JButton getBtnRegister() {
        return btnRegister;
    }

    public JTextField getTxtUserName() {
        return txtUserName;
    }

    public JPasswordField getTxtPassword() {
        return txtPassword;
    }

    public LoginScreen() {
        this.setSize(300, 150);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        this.getContentPane().add(new JLabel("Library Management System"));

        JPanel main = new JPanel(new SpringLayout());
        main.add(new JLabel("Username:"));
        main.add(txtUserName);
        main.add(new JLabel("Password:"));
        main.add(txtPassword);

        SpringUtilities.makeCompactGrid(main,
                2, 2, 
                6, 6,    
                6, 6);    

        this.getContentPane().add(main);

        // Add both Login and Register buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);
        this.getContentPane().add(buttonPanel);

        btnLogin.addActionListener(this);
        btnRegister.addActionListener(this); // Register button action listener
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
        	User user = new User();
        	user.setUsername(txtUserName.getText().trim());
    		user.setPassword(txtPassword.getText().trim());
    		String userBody = gson.toJson(user);
    		try {
    		    User loggedInUser = Application.getInstance().getDataAdapter().login(userBody);

    		    if (loggedInUser.getUserId() == 0) {
    		        JOptionPane.showMessageDialog(null, "This user does not exist!");
    		    } else {
    		        Application.getInstance().setCurrentUser(loggedInUser);
    		        if(loggedInUser.getUserId() == 3000) {
    		        	AddBooksScreen addBooksScreen = new AddBooksScreen();
    		        	setVisible(false);
    		        	addBooksScreen.setVisible(true);
    		        }
    		        else {
    		        	BookDashboardScreen bookDashboardScreen = new BookDashboardScreen();
        		        setVisible(false);
        		        bookDashboardScreen.setVisible(true);
    		        }
    		    }
    		} catch (IOException ex) {
    		    ex.printStackTrace();
    		}
    		
        } else if (e.getSource() == btnRegister) {
            RegistrationScreen registrationScreen = new RegistrationScreen();
            registrationScreen.setVisible(true);
            this.setVisible(false);
        }
    }
}
