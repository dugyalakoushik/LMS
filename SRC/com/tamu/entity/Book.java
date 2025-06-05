package com.tamu.entity;
import org.bson.Document;

public class Book {

	private int bookId;
	private String bookName;
	private String bookDescription;
	private String authorName;
	private double bookPrice;
	private int quantity;
	private String genre;
	private String imageURL;
	
	
	public String getImageURL() {
		return imageURL;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getBookDescription() {
		return bookDescription;
	}
	public void setBookDescription(String bookDescription) {
		this.bookDescription = bookDescription;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorId) {
		this.authorName = authorId;
	}
	public double getBookPrice() {
		return bookPrice;
	}
	public void setBookPrice(double bookPrice) {
		this.bookPrice = bookPrice;
	}
    public Document toDocument() {

        return new Document("bookId", bookId)
                .append("bookName", bookName)
				.append("bookDescription", bookDescription)
                .append("authorName", authorName)
                .append("quantity", quantity)
                .append("bookPrice", bookPrice)
                .append("genre", genre)
                .append("imageURL", imageURL);
    }

    public static Book fromDocument(Document document) {
        Book book = new Book();
        book.setBookId(document.getInteger("bookId"));
        book.setBookName(document.getString("bookName"));
        book.setBookDescription(document.getString("bookDescription"));
        book.setAuthorName(document.getString("authorName"));
        book.setBookPrice(document.getDouble("bookPrice"));
        book.setQuantity(document.getInteger("quantity"));
        book.setGenre(document.getString("genre"));
        book.setImageURL(document.getString("imageURL"));
        return book;
    }
	
	
}
