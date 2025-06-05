package com.tamu.gui;
import javax.swing.*;

import com.google.gson.Gson;
import com.tamu.dto.MembershipDto;
import com.tamu.entity.Address;
import com.tamu.entity.Card;
import com.tamu.entity.Membership;
import com.tamu.entity.MembershipType;
import com.tamu.entity.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MembershipScreen extends JFrame implements ActionListener {
    private List<MembershipType> memberships;

    // Change the type of cmbMemberships to JComboBox<String>
    private JComboBox<String> cmbMemberships = new JComboBox<>();

    private JTextField txtLineOne = new JTextField(20);
    private JTextField txtCity = new JTextField(20);
    private JTextField txtState = new JTextField(20);
    private JTextField txtZip = new JTextField(20);
    private JTextField txtCountry = new JTextField(20);
    private JTextField txtContact = new JTextField(20);

    private JTextField txtCardNumber = new JTextField(20);
    private JTextField txtCardName = new JTextField(20);
    private JTextField txtStartDate = new JTextField(20);
    private JTextField txtEndDate = new JTextField(20);
    private JTextField txtCvv = new JTextField(20);
    private JButton btnBack = new JButton("Back");

    private JButton btnSelectMembership = new JButton("Add Membership");

    public MembershipScreen() {
    	
        this.memberships = Application.getInstance().getDataAdapter().retrieveMemberships();
        
        this.setTitle("Membership Selection");
        this.setSize(600, 500);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Populate the membership dropdown with type names
        cmbMemberships.addItem("Platinum Membership");
        cmbMemberships.addItem("Gold Membership");
        cmbMemberships.addItem("Silver Membership");

        mainPanel.add(createLabelAndComponent("Select Membership:", cmbMemberships));
        mainPanel.add(createLabelAndComponent("Address Line 1:", txtLineOne));
        mainPanel.add(createLabelAndComponent("City:", txtCity));
        mainPanel.add(createLabelAndComponent("State:", txtState));
        mainPanel.add(createLabelAndComponent("ZIP Code:", txtZip));
        mainPanel.add(createLabelAndComponent("Country:", txtCountry));
        mainPanel.add(createLabelAndComponent("Contact Number:", txtContact));

        // Align "Enter Card Details" label to the left
        JPanel cardDetailsLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cardDetailsLabelPanel.add(new JLabel("Enter Card Details:"));
        mainPanel.add(cardDetailsLabelPanel);

        mainPanel.add(createLabelAndComponent("Card Number:", txtCardNumber));
        mainPanel.add(createLabelAndComponent("Card Holder's Name:", txtCardName));
        mainPanel.add(createLabelAndComponent("Card Start Date (mm-dd-yyyy):", txtStartDate));
        mainPanel.add(createLabelAndComponent("Card End Date (mm-dd-yyyy):", txtEndDate));
        mainPanel.add(createLabelAndComponent("CVV:", txtCvv));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(btnBack);
        buttonPanel.add(btnSelectMembership);

        this.add(mainPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);

        btnSelectMembership.addActionListener(this);
        btnBack.addActionListener(this);
    }

    private JPanel createLabelAndComponent(String labelText, JComponent component) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(200, 20));
        panel.add(label);
        panel.add(component);
        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnSelectMembership) {
            // Retrieve selected membership details
        	String cardNumber = txtCardNumber.getText();
        	String cardName = txtCardName.getText();
        	Date startDate = parseDate(txtStartDate.getText()); // You will need to write a method to parse the date from String
        	Date endDate = parseDate(txtEndDate.getText()); // Similarly, a method to parse this date
        	String cvv = txtCvv.getText();

        	Card newCard = new Card();
        	newCard.setCardNumber(cardNumber);
        	newCard.setCardName(cardName);
        	newCard.setStartDate(startDate);
        	newCard.setEndDate(endDate);
        	newCard.setCvv(cvv);

        	// Creating a new Address object
        	String lineOne = txtLineOne.getText();
        	String city = txtCity.getText();
        	String state = txtState.getText();
        	String zip = txtZip.getText();
        	String country = txtCountry.getText();
        	String contact = txtContact.getText();

        	Address newAddress = new Address();
        	newAddress.setLineOne(lineOne);
        	newAddress.setCity(city);
        	newAddress.setState(state);
        	newAddress.setZip(zip);
        	newAddress.setCountry(country);
        	newAddress.setContact(contact);
        	
        	User user = Application.getInstance().getCurrentUser();
        	
        	newAddress.setUserId(user.getUserId());
        	newCard.setUserId(user.getUserId());
        	
            String selectedMembershipType = (String) cmbMemberships.getSelectedItem();
            
            
            MembershipType selectedMembership = findMembershipByType(selectedMembershipType);
            
            user.setMembership(selectedMembership);        
            
            MembershipDto dto= new MembershipDto();
            dto.setAddress(newAddress);
            dto.setCard(newCard);
            dto.setUser(user);
            Gson gson = new Gson();
            User updatedUser = null;   
            try {
            	updatedUser = Application.getInstance().getDataAdapter().createMembership(gson.toJson(dto));
                if (updatedUser != null) {
                	Application.getInstance().setCurrentUser(updatedUser);
                }
            	String receiptText = "						Membership Receipt \n \nMembership Details \nMembership Type : " + selectedMembershipType
        		+ "\n\nBilling Address \nLine 1 : " + lineOne
        		+ "\nCity : " + city
        		+ "\nState : " + state
        		+ "\nZip : " + zip
        		+ "\nContact : " + contact
        		+ "\n\nCard Details \nCard Number : " + cardNumber
        		+ "\nCard Name : " + cardName
            	+ "\n\nCard Details \nMembership Cost : $" + selectedMembership.getPrice()
            	+ "\nTax% : " + "$8.5"
            	+ "\nTotal Amount : $" + selectedMembership.getPrice()* 1.085;
            	JOptionPane.showMessageDialog(this, "Membership Created Successfully! \n" + receiptText);
            	BookDashboardScreen bookDashboardScreen = new BookDashboardScreen();
                setVisible(false);
                bookDashboardScreen.setVisible(true);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

        }
        else if (e.getSource() == btnBack) {
            try {
            	BookDashboardScreen bookDashboardScreen = new BookDashboardScreen();
                setVisible(false);
                bookDashboardScreen.setVisible(true);            	
            }catch (IOException ex) {
    		    ex.printStackTrace();
    		}
        }
    }

    // Helper method to find MembershipType by type
    private MembershipType findMembershipByType(String membershipType) {
        for (MembershipType membership : memberships) {
            if (membership.getType().equals(membershipType)) {
                return membership;
            }
        }
        return null; // Handle appropriately if not found
    }
    
    public static Date parseDate(String dateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        try {
            return formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Or handle the error as appropriate
        }
    }

}
