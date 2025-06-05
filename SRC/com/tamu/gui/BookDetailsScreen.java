package com.tamu.gui;
import javax.swing.*;

import com.google.gson.Gson;
import com.tamu.entity.Book;
import com.tamu.entity.Subscription;
import com.tamu.entity.User;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class BookDetailsScreen extends JFrame implements ActionListener {
    private JLabel lblBookTitle = new JLabel();
    private JLabel lblAuthor = new JLabel();
    private JLabel lblGenre = new JLabel();
    private JLabel lblDescription = new JLabel();
    private JLabel lblPrice = new JLabel();
    private JLabel lblQuantity = new JLabel();
    private JButton btnBack = new JButton("Back to Dashboard");
    private JButton btnRent = new JButton("Rent");
    private Book book;
    public BookDetailsScreen(Book book) {
    	this.book = book;
        this.setTitle("Book Details");
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.setSize(400, 300);

        lblBookTitle.setText("Title: " + book.getBookName());
        lblAuthor.setText("Author: " + book.getAuthorName());
        lblGenre.setText("Genre: " + book.getGenre());
        lblDescription.setText("Description: " + book.getBookDescription());
        lblPrice.setText("Price: $" + book.getBookPrice());
        lblQuantity.setText("Quantity Available: " + book.getQuantity());

        this.getContentPane().add(lblBookTitle);
        this.getContentPane().add(lblAuthor);
        this.getContentPane().add(lblGenre);
        this.getContentPane().add(lblDescription);
        this.getContentPane().add(lblPrice);
        this.getContentPane().add(lblQuantity);
        this.getContentPane().add(btnBack);

        btnBack.addActionListener(this);
        this.getContentPane().add(btnRent);
        updateRentButton();    
        btnRent.addActionListener(this);    
    }
    
    private void updateRentButton() {
        boolean userHasMembership = Application.getInstance().getCurrentUser().getMembershipId() == 0;
        btnRent.setVisible(!userHasMembership);
        btnRent.setEnabled(!userHasMembership);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnBack) {
            BookDashboardScreen bookDashboardScreen = null;
			try {
				bookDashboardScreen = new BookDashboardScreen();
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            setVisible(false);
            bookDashboardScreen.setVisible(true);
        }
        else if (e.getSource() == btnRent) {
            rentBook();
        }
    }
    
    private void rentBook() {
        try {
        	Subscription newSubscription = new Subscription();
        	User user  = Application.getInstance().getCurrentUser();
        	newSubscription.setUser(Application.getInstance().getCurrentUser());
        	newSubscription.setBook(this.book);
            
        	  Random random = new Random();

              // The range for the random number is 10000 (inclusive) to 100000 (exclusive)
            int randomNumber = 10000 + random.nextInt(90000); // 99999 - 10000 = 89999 + 1 = 90000
             newSubscription.setSubscriptionId(randomNumber);
             
            int days = user.getMembership().getValidity();
            LocalDate dueDate = LocalDate.now().plusDays(days);
     		LocalDate orderDate = LocalDate.now();
     		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
     		String dueD = dueDate.format(formatter);
     		String orderD = orderDate.format(formatter);
            newSubscription.setDueDate(dueD);
            newSubscription.setOrderDate(orderD);
            Gson gson = new Gson();
            
            // Call the subscribe method in the RemoteDataAdapter
            String response = Application.getInstance().getDataAdapter().subscribe(gson.toJson(newSubscription));

            // Check the response and show a dialog accordingly
            if (response.startsWith("Subscribed Successfully")) {
                JOptionPane.showMessageDialog(this, "Book Subscribed Successfully");
                // Redirect to BookDashboardScreen
                BookDashboardScreen bookDashboardScreen = new BookDashboardScreen();
                setVisible(false);
                bookDashboardScreen.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Error Subscribing: Failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error Subscribing: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
