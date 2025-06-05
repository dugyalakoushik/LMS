//package com.tamu.dao;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//
//import com.tamu.dto.MyBookDto;
//import com.tamu.entity.Address;
//import com.tamu.entity.Book;
//import com.tamu.entity.Card;
//import com.tamu.entity.Membership;
//import com.tamu.entity.Subscription;
//import com.tamu.entity.User;
//import com.tamu.entity.MembershipType;
//
//public class ApplicationDao {
//
//	public List<Book> searchBook(String searchType, String searchString, int page) throws ClassNotFoundException {
//		Book book = null;
//		List<Book> books = new ArrayList<>();
//		int pageSize = page * 6;
//		try {
//			Connection connection = DBConnection.getConnectionToDataBase();
//
//			String sql = "select * from books where " + searchType + " like '%" + searchString + "%'offset " + pageSize
//					+ " limit 6;";
//
//			Statement statement = connection.createStatement();
//
//			ResultSet set = statement.executeQuery(sql);
//
//			while (set.next()) {
//				book = new Book();
//				book.setBookId(set.getInt("book_id"));
//				book.setBookName(set.getString("book_name"));
//				book.setBookDescription(set.getString("book_description"));
//				book.setGenre(set.getString("genre"));
//				book.setAuthorName(set.getString("author_id"));
//				books.add(book);
//
//			}
//
//		} catch (SQLException exception) {
//			exception.printStackTrace();
//		}
//		return books;
//	}
//
//	public List<Book> searchBookByName(String searchString, int page) throws ClassNotFoundException {
//		Book book = null;
//		List<Book> books = new ArrayList<>();
//		int pageSize = page * 6;
//
//		try {
//			Connection connection = DBConnection.getConnectionToDataBase();
//
//			String sql = "select * from books where book_name like '%" + searchString + "%'offset " + pageSize
//					+ " limit 6;";
//			System.out.println((sql));
//			Statement statement = connection.createStatement();
//
//			ResultSet set = statement.executeQuery(sql);
//
//			while (set.next()) {
//				book = new Book();
//				book.setBookId(set.getInt("book_id"));
//				book.setBookName(set.getString("book_name"));
//				book.setBookDescription(set.getString("book_description"));
//				book.setGenre(set.getString("genre"));
//				book.setAuthorName(set.getString("author_id"));
//				book.setImageURL(set.getString("image_url"));
//				books.add(book);
//
//			}
//
//		} catch (SQLException exception) {
//			exception.printStackTrace();
//		}
//		return books;
//	}
//
//	public int insertOrder(Subscription order, String username) throws ClassNotFoundException {
//		int rowsAffected = 0;
//
//		try {
//			Connection connection = DBConnection.getConnectionToDataBase();
//
//			String sql = "INSERT INTO public.subscriptions(book_id, user_id, order_date, due_date, is_returned) VALUES (?, (select user_id from users where username = ?) , '" + order.getOrderDate()+ "', '" + order.getDueDate() +"', 0);";
//			System.out.println((sql));
//
//			java.sql.PreparedStatement statement = connection.prepareStatement(sql);
//			statement.setLong(1, order.getBookId());
//			statement.setString(2, username);
//
//
//			rowsAffected = statement.executeUpdate();
//		} catch (SQLException exception) {
//			exception.printStackTrace();
//		}
//		return rowsAffected;
//	}
//
//	public List<MyBookDto> getSubscription(String username) throws ClassNotFoundException {
//		
//		MyBookDto dto = null;
//		List<MyBookDto> dtos = new ArrayList<>();
//		try {
//			Connection connection = DBConnection.getConnectionToDataBase();
//
//			String sql = "select * from subscriptions join books on subscriptions.book_id = books.book_id where user_id in(select user_id from users where username = '"+ username +"' ) and is_returned = 0 order by order_date desc;";
//
//			System.out.println((sql));
//			Statement statement = connection.createStatement();
//
//			ResultSet set = statement.executeQuery(sql);
//
//			while (set.next()) {
//				dto = new MyBookDto();
//				dto.setBookId(set.getInt("book_id"));
//				dto.setBookName(set.getString("book_name"));
//				dto.setBookDescription(set.getString("book_description"));
//				dto.setGenre(set.getString("genre"));
//				dto.setAuthorName(set.getString("author_id"));
//				dto.setDueDate(set.getDate("due_date").toString());
//				dto.setOrderDate(set.getDate("order_date").toString());
//				dtos.add(dto);
//			}
//
//		} catch (SQLException exception) {
//			exception.printStackTrace();
//		}
//		return dtos;
//		
//	}
//
//	public int getTotalBooks(String searchType, String searchString) throws ClassNotFoundException {
//		int totalCount = 0;
//		try {
//			Connection connection = DBConnection.getConnectionToDataBase();
//
//			String sql = "select count(*) from books where " + searchType + " like '%" + searchString + "%'";
//
//			Statement statement = connection.createStatement();
//
//			ResultSet set = statement.executeQuery(sql);
//
//			while (set.next()) {
//				totalCount = set.getInt(1);
//
//			}
//
//		} catch (SQLException exception) {
//			exception.printStackTrace();
//		}
//		return totalCount;
//	}
//	
//
//	public Book getBook(int bookId) throws ClassNotFoundException {
//		Book book = new Book();;
//		try {
//			Connection connection = DBConnection.getConnectionToDataBase();
//
//			String sql = "select * from books where book_id = " + bookId  +" ;";
//
//			Statement statement = connection.createStatement();
//
//			ResultSet set = statement.executeQuery(sql);
//
//			while (set.next()) {
//				book.setBookId(set.getInt("book_id"));
//				book.setBookName(set.getString("book_name"));
//				book.setBookDescription(set.getString("book_description"));
//				book.setGenre(set.getString("genre"));
//				book.setAuthorName(set.getString("author_id"));
//				book.setImageURL(set.getString("image_url"));
//			}
//
//		} catch (SQLException exception) {
//			exception.printStackTrace();
//		}
//		return book;
//	}
//
//	public int registerUser(User user) throws ClassNotFoundException {
//		int rowsAffected = 0;
//
//		try {
//			Connection connection = DBConnection.getConnectionToDataBase();
//
//			String insertQuery = "INSERT INTO users (username, password, first_name, last_name, age, activity, email_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
//
//			java.sql.PreparedStatement statement = connection.prepareStatement(insertQuery);
//			statement.setString(1, user.getUsername());
//			statement.setString(2, user.getPassword());
//			statement.setString(3, user.getFirstName());
//			statement.setString(4, user.getLastName());
//			statement.setInt(5, user.getAge());
//			statement.setString(6, user.getActivity());
//			statement.setString(7, user.getEmail());
//
//			rowsAffected = statement.executeUpdate();
//
//		} catch (SQLException exception) {
//			exception.printStackTrace();
//		}
//		return rowsAffected;
//	}
//
//
//	public User loadUser(String username, String password) throws ClassNotFoundException {
//		User user = null;
//		try {
//
//			Connection connection = DBConnection.getConnectionToDataBase();
//
//			String sql = "select * from users where username=? and password=?";
//
//			java.sql.PreparedStatement statement = connection.prepareStatement(sql);
//			statement.setString(1, username);
//			statement.setString(2, password);
//			ResultSet set = statement.executeQuery();
//			Integer membershipId = 0;
//			while (set.next()) {
//				user = new User();
//				user.setUserId(set.getDouble(1));
//				user.setUsername(set.getString(2));
//				user.setFirstName(set.getString(4));
//				user.setLastName(set.getString(5));
//			}
//			if (membershipId != 0) {
//				sql = "select * from membership_type where membership_type_id in ( select membership_type_id from membership where user_id = " + membershipId + " order by end_date)";
//				set = statement.executeQuery();
//				while (set.next()) {
//					MembershipType membership = new MembershipType();
//					membership.setMembershipId(set.getDouble(1));
//					membership.setType(set.getString(2));
//					membership.setNumBooks(set.getInt(3));
//					membership.setValidity(set.getInt(4));
//					membership.setPrice(set.getDouble(5));
//					user.setMembership(membership);
//				}
//				
//			}
//			
//		} catch (SQLException exception) {
//			exception.printStackTrace();
//		}
//		return user;
//	}
//	public User loadUser(double userId) throws ClassNotFoundException {
//		User user = null;
//		try {
//
//			Connection connection = DBConnection.getConnectionToDataBase();
//
//			String sql = "select * from users where user_id=?";
//
//			java.sql.PreparedStatement statement = connection.prepareStatement(sql);
//			statement.setDouble(1, userId);
//			ResultSet set = statement.executeQuery();
//			Integer membershipId = 0;
//			while (set.next()) {
//				user = new User();
//				user.setUserId(set.getDouble(1));
//				user.setUsername(set.getString(2));
//				user.setFirstName(set.getString(4));
//				user.setLastName(set.getString(5));
//			}
//			if (membershipId != 0) {
//				sql = "select * from membership_type where membership_type_id in ( select membership_type_id from membership where user_id = " + membershipId + " order by end_date)";
//				set = statement.executeQuery();
//				while (set.next()) {
//					MembershipType membership = new MembershipType();
//					membership.setMembershipId(set.getDouble(1));
//					membership.setType(set.getString(2));
//					membership.setNumBooks(set.getInt(3));
//					membership.setValidity(set.getInt(4));
//					membership.setPrice(set.getDouble(5));
//					user.setMembership(membership);
//				}
//				
//			}
//			
//		} catch (SQLException exception) {
//			exception.printStackTrace();
//		}
//		return user;
//	}
//	public Membership getMembership(double userId) throws ClassNotFoundException {
//		Membership membership = null;
//		try {
//
//			Connection connection = DBConnection.getConnectionToDataBase();
//
//			String sql = "select mt.membership_type_name, m.end_date, mt.price from membership as m join membership_type as mt on m.membership_type_id = mt.membership_type_id where m.user_id = ? order by end_date";
//
//			java.sql.PreparedStatement statement = connection.prepareStatement(sql);
//			statement.setDouble(1, userId);
//			ResultSet set = statement.executeQuery();
//			
//			while (set.next()) {
//				membership = new Membership();
//				MembershipType mt = new MembershipType();
//				mt.setType(set.getString(1));
//				mt.setPrice(set.getDouble(3));
//				membership.setMembershipType(mt);
//				membership.setEndDate(new Date(set.getDate(2).getTime()));
//			}
//			
//		} catch (SQLException exception) {
//			exception.printStackTrace();
//		}
//		return membership;
//	}
//	public List<MembershipType> getMemberships() throws ClassNotFoundException {
//		List<MembershipType> memberships = new ArrayList<MembershipType>();
//		try {
//
//			Connection connection = DBConnection.getConnectionToDataBase();
//
//			String sql = "select * from membership_type";
//
//			java.sql.PreparedStatement statement = connection.prepareStatement(sql);
//
//			ResultSet set = statement.executeQuery();
//			
//			while (set.next()) {
//				MembershipType membership = new MembershipType();
//				membership.setMembershipId(set.getDouble(1));
//				membership.setType(set.getString(2));
//				membership.setNumBooks(set.getInt(3));
//				membership.setValidity(set.getInt(4));
//				membership.setPrice(set.getDouble(5));
//				memberships.add(membership);
//			}
//			
//		} catch (SQLException exception) {
//			exception.printStackTrace();
//		}
//		return memberships;
//	}
//	public Address insertAddress(Address address) throws ClassNotFoundException {
//		try {
//			Connection connection = DBConnection.getConnectionToDataBase();
//
//			String insertQuery = "insert into address values(default,?,?,?,?,?,?) returning addressId";
//
//			java.sql.PreparedStatement statement = connection.prepareStatement(insertQuery);
//			statement.setString(1, address.getLineOne());
//			statement.setString(2, address.getCity());
//			statement.setString(3, address.getState());
//			statement.setString(4, address.getZip());
//			statement.setString(5, address.getCountry());
//			statement.setString(6, address.getContact());
//
//			ResultSet set = statement.executeQuery();
//			while (set.next()) {
//				address.setAddressId(set.getInt(1));
//				return address;
//
//			}
//		} catch (SQLException exception) {
//			exception.printStackTrace();
//		}
//		return null;
//	}
//	public Card insertCard(Card card) throws ClassNotFoundException {
//		try {
//			Connection connection = DBConnection.getConnectionToDataBase();
//
//			String insertQuery = "insert into card values(default,?,?,?,?,?) RETURNING cardId";
//
//			java.sql.PreparedStatement statement = connection.prepareStatement(insertQuery);
//			statement.setString(1, card.getCardNumber());
//			statement.setDate(2, new java.sql.Date(card.getStartDate().getTime()));
//			statement.setDate(3, new java.sql.Date(card.getEndDate().getTime()));
//			statement.setString(5, card.getCvv());
//			statement.setString(4, card.getCardName());
//			ResultSet set = statement.executeQuery();
//			while (set.next()) {
//				card.setCardID(set.getDouble(1));
//				return card;
//
//			}
//		} catch (SQLException exception) {
//			exception.printStackTrace();
//		}
//		return null;
//	}
//	public Membership createMembership(Membership membership) throws ClassNotFoundException {
//		try {
//			Connection connection = DBConnection.getConnectionToDataBase();
//
//			String insertQuery = "insert into membership values(default,?,?,?,?) RETURNING membership_id";
//   
//			java.sql.PreparedStatement statement = connection.prepareStatement(insertQuery);
//			statement.setDouble(1, membership.getMembershipType().getMembershipId());
//			statement.setDouble(2, membership.getUser().getUserId());
//			statement.setDate(3, new java.sql.Date(membership.getStartDate().getTime()));
//			statement.setDate(4, new java.sql.Date(membership.getEndDate().getTime()));
//
//			ResultSet rset = statement.executeQuery();
//			while (rset.next()) {
//				membership.setMembershipId(rset.getDouble(1));
//				return membership;
//
//			}
//		} catch (SQLException exception) {
//			exception.printStackTrace();
//		}
//		return null;
//	}
//	public int getSubscriptionsByUserId(double userId) throws ClassNotFoundException {
//		int subs = 0 ;
//		try {
//
//			Connection connection = DBConnection.getConnectionToDataBase();
//
//			String sql = "select count(*) as count from subscriptions where user_id = ? and is_returned = 0";
//
//			java.sql.PreparedStatement statement = connection.prepareStatement(sql);
//			statement.setDouble(1,userId);
//			ResultSet set = statement.executeQuery();
//			
//			while (set.next()) {
//				subs = set.getInt(1);
//			}
//			
//		} catch (SQLException exception) {
//			exception.printStackTrace();
//		}
//		return subs;
//	}
//	
//	public int getNumBooksByUser(double userId) throws ClassNotFoundException {
//		int num = 0 ;
//		try {
//
//			Connection connection = DBConnection.getConnectionToDataBase();
//
//			String sql = "select num_of_books from membership_type where membership_type_id in (select membership_type_id from membership where user_id = ?)";
//
//			java.sql.PreparedStatement statement = connection.prepareStatement(sql);
//			statement.setDouble(1,userId);
//
//			ResultSet set = statement.executeQuery();
//			
//			while (set.next()) {
//				num = set.getInt(1);
//			}
//			
//		} catch (SQLException exception) {
//			exception.printStackTrace();
//		}
//		return num;
//	}
//	
//	public MembershipType getMembershipType(double membershipTypeId) throws ClassNotFoundException {
//		MembershipType mt = null;
//		try {
//
//			Connection connection = DBConnection.getConnectionToDataBase();
//
//			String sql = "select * from membership_type where membership_type_id = ?";
//
//			java.sql.PreparedStatement statement = connection.prepareStatement(sql);
//			statement.setDouble(1,membershipTypeId);
//
//			ResultSet set = statement.executeQuery();
//			
//			while (set.next()) {
//				mt = new MembershipType();
//				mt.setMembershipId(set.getDouble(1));
//				mt.setType(set.getString(2));
//				mt.setNumBooks(set.getInt(3));
//				mt.setValidity(set.getInt(4));
//				mt.setPrice(set.getDouble(5));
//			}
//			
//		} catch (SQLException exception) {
//			exception.printStackTrace();
//		}
//		return mt;
//	}
//	public boolean isUsernameTaken(String username) throws ClassNotFoundException, SQLException {
//		Connection connection = DBConnection.getConnectionToDataBase();
//	    String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
//	    
//	    try (PreparedStatement statement = connection.prepareStatement(sql)) {
//	        statement.setString(1, username);
//	        ResultSet resultSet = statement.executeQuery();
//	        
//	        if (resultSet.next()) {
//	            int count = resultSet.getInt(1);
//	            return count > 0;
//	        }
//	    } catch (SQLException e) {
//	        e.printStackTrace();
//	    }
//	    
//	    return false;
//	}
//	public boolean isEmailTaken(String email) throws ClassNotFoundException, SQLException {
//	    Connection connection = DBConnection.getConnectionToDataBase();
//	    String sql = "SELECT COUNT(*) FROM users WHERE email_id = ?";
//	    
//	    try (PreparedStatement statement = connection.prepareStatement(sql)) {
//	        statement.setString(1, email);
//	        ResultSet resultSet = statement.executeQuery();
//	        
//	        if (resultSet.next()) {
//	            int count = resultSet.getInt(1);
//	            return count > 0;
//	        }
//	    } catch (SQLException e) {
//	        e.printStackTrace();
//	    }
//	    
//	    return false;
//	}
//	public boolean addBook(Book book) throws ClassNotFoundException, SQLException{
//		
//		Connection connection = DBConnection.getConnectionToDataBase();
//		
//		try {
//		
//			String insertQuery = "INSERT INTO books (book_name, book_description, book_price, author_id, genre, quantity, image_url) VALUES (?, ?, ?, ?, ?, ?, ?)";
//
//			java.sql.PreparedStatement statement = connection.prepareStatement(insertQuery);
//			statement.setString(1, book.getBookName());
//			statement.setString(2, book.getBookDescription());
//			statement.setDouble(3, book.getBookPrice());
//			statement.setString(4, book.getAuthorName());
//			statement.setString(5, book.getGenre());
//			statement.setInt(6, book.getQuantity());
//			statement.setString(7, "");
//
//			int rowsEffected = statement.executeUpdate();
//			System.out.println("Rows effected: "+rowsEffected);
//			return true;
//		}catch(SQLException e) {
//			e.printStackTrace();
//			return false;
//		}
//		
//	}
//	public boolean validateUser(String username, String password) throws ClassNotFoundException {
//		boolean isValidUser = false;
//		try {
//
//			Connection connection = DBConnection.getConnectionToDataBase();
//
//			String sql = "select * from users where username=? and password=?";
//
//			java.sql.PreparedStatement statement = connection.prepareStatement(sql);
//			statement.setString(1, username);
//			statement.setString(2, password);
//
//			ResultSet set = statement.executeQuery();
//			while (set.next()) {
//				isValidUser = true;
//			}
//		} catch (SQLException exception) {
//			exception.printStackTrace();
//		}
//		return isValidUser;
//	}
//}
