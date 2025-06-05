package com.tamu.gui;
import javax.swing.*;

import com.tamu.entity.Book;
import com.tamu.entity.MembershipType;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class BookDashboardScreen extends JFrame implements ActionListener {
    private JTextField txtSearchTerm = new JTextField(20);
    private JRadioButton radioTitle = new JRadioButton("Search by Title");
    private JRadioButton radioAuthor = new JRadioButton("Search by Author");
    private JButton btnSearch = new JButton("Search");
    private JButton btnGetMembership = new JButton("Get Membership");

    // Sample books
    private List<Book> sampleBooks;
    private JPanel panelBooks;
    private int currentPage = 0;
    private int pageSize = 6;

    public BookDashboardScreen() throws UnknownHostException, IOException {
        this.setTitle("Book Dashboard");
        this.setLayout(new BorderLayout());
        this.setSize(800, 600);
        JPanel panelTopButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnHome = new JButton("Home");
        JButton btnMyBooks = new JButton("My Books");
        JButton btnLogOut = new JButton("Log Out");

        sampleBooks = Application.getInstance().getDataAdapter().getBooks();

        // Panel for search components
        JPanel panelSearch = new JPanel();
        panelSearch.add(new JLabel("Search Term:"));
        panelSearch.add(txtSearchTerm);
        panelSearch.add(radioTitle);
        panelSearch.add(radioAuthor);
        panelSearch.add(btnSearch);
        btnSearch.addActionListener(this);
        ButtonGroup group = new ButtonGroup();
        group.add(radioTitle);
        group.add(radioAuthor);

        // Panel for 'Get Membership' button
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelButtons.add(btnGetMembership);
        btnGetMembership.addActionListener(this);
        updateMembershipButton();

        // Panel for book display
        panelBooks = new JPanel(new GridLayout(2, 3, 10, 10));
        updateBookPanel();
        JScrollPane scrollPane = new JScrollPane(panelBooks);

        btnHome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Redirect to BookDashboardScreen
                try {
                    BookDashboardScreen bookDashboardScreen = new BookDashboardScreen();
                    setVisible(false);
                    bookDashboardScreen.setVisible(true);
                } catch (UnknownHostException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        panelTopButtons.add(btnHome, BorderLayout.WEST);

        btnMyBooks.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Redirect to MyBooksScreen
                MyBooksScreen myBooksScreen;
				try {
					myBooksScreen = new MyBooksScreen();
	                setVisible(false);
	                myBooksScreen.setVisible(true);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        panelTopButtons.add(btnMyBooks, BorderLayout.CENTER);

        btnLogOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Redirect to LogInScreen
                LoginScreen logInScreen = new LoginScreen();
                setVisible(false);
                logInScreen.setVisible(true);
            }
        });
        panelTopButtons.add(btnLogOut, BorderLayout.EAST);

        // Combine search, button, and top button panels
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(panelTopButtons, BorderLayout.NORTH);
        topPanel.add(panelSearch, BorderLayout.CENTER);
        topPanel.add(panelButtons, BorderLayout.SOUTH);

        // Combine all panels in the main layout
        this.add(topPanel, BorderLayout.NORTH);
        this.add(panelBooks, BorderLayout.CENTER);
        
        JButton btnPrevPage = new JButton("Previous Page");
        JButton btnNextPage = new JButton("Next Page");

        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        paginationPanel.add(btnPrevPage);
        paginationPanel.add(btnNextPage);

        this.add(paginationPanel, BorderLayout.SOUTH);

        // Add action listeners for pagination buttons
        btnPrevPage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPage > 0) {
                    currentPage--;
                    updateBookPanel();
                }
            }
        });

        btnNextPage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int maxPage = (int) Math.ceil((double) sampleBooks.size() / pageSize);
                if (currentPage < maxPage - 1) {
                    currentPage++;
                    updateBookPanel();
                }
            }
        });
    }


 // Method to create a panel for displaying book details
    private JPanel createBookPanel(Book book) {
        JPanel bookPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(book.getBookName());
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bookPanel.add(titleLabel, BorderLayout.NORTH);

        // Load the image using ImageIcon
        ImageIcon imageIcon = new ImageIcon(book.getImageURL());
        JLabel imageLabel = new JLabel(imageIcon);
        bookPanel.add(imageLabel, BorderLayout.CENTER);

        // You can add more labels or components to display other details like author, price, etc.

        JButton detailsButton = new JButton("View Details");
        detailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BookDetailsScreen bookDetailsScreen = new BookDetailsScreen(book);
                setVisible(false);
                bookDetailsScreen.setVisible(true);
            }
        });
        bookPanel.add(detailsButton, BorderLayout.SOUTH);

        return bookPanel;
    }

    private void updateMembershipButton() {
        boolean userHasMembership = Application.getInstance().getCurrentUser().getMembershipId() != 0;
        btnGetMembership.setVisible(!userHasMembership);
        btnGetMembership.setEnabled(!userHasMembership);
    }
    
    private void updateBookPanel() {
        panelBooks.removeAll(); // Clear existing books

        int startIndex = currentPage * pageSize;
        int endIndex = Math.min(startIndex + pageSize, sampleBooks.size());

        for (int i = startIndex; i < endIndex; i++) {
            Book book = sampleBooks.get(i);
            book.setImageURL("src/images/book.png");
            panelBooks.add(createBookPanel(book));
        }

        panelBooks.revalidate();
        panelBooks.repaint();
    }

    public void actionPerformed(ActionEvent e) {
    	if (e.getSource() == btnSearch) {
    		 String searchTerm = txtSearchTerm.getText();
             boolean searchByTitle = radioTitle.isSelected();
             boolean searchByAuthor = radioAuthor.isSelected();

             if (!searchTerm.isEmpty() && (searchByTitle || searchByAuthor)) {
                 try {
                     // Update sampleBooks with search results
                     sampleBooks = Application.getInstance().getDataAdapter().getBooks(
                             (searchByTitle ? "bookName" : "authorName"), searchTerm);
                     currentPage = 0; // Reset to the first page
                     updateBookPanel();
                 } catch (UnknownHostException ex) {
                     ex.printStackTrace();
                 } catch (IOException ex) {
                     ex.printStackTrace();
                 }
             } else {
                 // Handle the case where search criteria are not provided
                 JOptionPane.showMessageDialog(this, "Please enter search term and select search criteria.",
                         "Invalid Search", JOptionPane.ERROR_MESSAGE);
             }
        } else if (e.getSource() == btnGetMembership) {
            MembershipScreen membershipScreen = new MembershipScreen();
            setVisible(false);
            membershipScreen.setVisible(true);
        }
    }
}
