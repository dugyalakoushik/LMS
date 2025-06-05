package com.tamu.gui;

import com.tamu.entity.Book;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class AddBooksScreen extends JFrame implements ActionListener {

    private JTextField txtBookName = new JTextField(20);
    private JTextField txtBookDescription = new JTextField(20);
    private JTextField txtAuthorName = new JTextField(20);
    private JTextField txtQuantity = new JTextField(20);
    private JTextField txtBookPrice = new JTextField(20);
    private JTextField txtGenre = new JTextField(20);
    private JTextField txtImageURL = new JTextField(20);

    private JButton btnAddBook = new JButton("Add Book");
    private JButton btnBack = new JButton("Back to Dashboard");

    public AddBooksScreen() {
        this.setTitle("Add Books");
        this.setSize(400, 300);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainPanel.add(createLabelAndComponent("Book Name:", txtBookName));
        mainPanel.add(createLabelAndComponent("Book Description:", txtBookDescription));
        mainPanel.add(createLabelAndComponent("Author Name:", txtAuthorName));
        mainPanel.add(createLabelAndComponent("Quantity:", txtQuantity));
        mainPanel.add(createLabelAndComponent("Book Price:", txtBookPrice));
        mainPanel.add(createLabelAndComponent("Genre:", txtGenre));
//        mainPanel.add(createLabelAndComponent("Image URL:", txtImageURL));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnBack);
        buttonPanel.add(btnAddBook);

        this.add(mainPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);

        btnAddBook.addActionListener(this);
        btnBack.addActionListener(this);
    }

    private JPanel createLabelAndComponent(String labelText, JTextField textField) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(150, 20));
        panel.add(label);
        panel.add(textField);
        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAddBook) {
            addBook();
        } else if (e.getSource() == btnBack) {
            try {
                BookDashboardScreen bookDashboardScreen = new BookDashboardScreen();
                setVisible(false);
                bookDashboardScreen.setVisible(true);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void addBook() {
        try {
            // Retrieve values from text fields
            String bookName = txtBookName.getText();
            String bookDescription = txtBookDescription.getText();
            String authorName = txtAuthorName.getText();
            int quantity = Integer.parseInt(txtQuantity.getText());
            double bookPrice = Double.parseDouble(txtBookPrice.getText());
            String genre = txtGenre.getText();
            String imageURL = "src/images/book.png";

            // Create a new Book object
            Book newBook = new Book();
            newBook.setBookName(bookName);
            newBook.setBookDescription(bookDescription);
            newBook.setAuthorName(authorName);
            newBook.setQuantity(quantity);
            newBook.setBookPrice(bookPrice);
            newBook.setGenre(genre);
            newBook.setImageURL(imageURL);

            // Add the new book to the database
//            Application.getInstance().getDataAdapter().addBook(newBook);

            // Show a success message
            JOptionPane.showMessageDialog(this, "Book added successfully!");

            // Clear the text fields
            clearTextFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity or price. Please enter numeric values.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
        } 
//        catch (IOException ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Error adding book: " + ex.getMessage(),
//                    "Error", JOptionPane.ERROR_MESSAGE);
//        }
    }

    private void clearTextFields() {
        txtBookName.setText("");
        txtBookDescription.setText("");
        txtAuthorName.setText("");
        txtQuantity.setText("");
        txtBookPrice.setText("");
        txtGenre.setText("");
        txtImageURL.setText("");
    }
}
