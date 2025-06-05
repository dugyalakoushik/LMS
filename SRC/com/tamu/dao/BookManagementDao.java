package com.tamu.dao;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.google.gson.Gson;
import com.tamu.entity.Book;
import com.tamu.entity.MembershipType;

import redis.clients.jedis.Jedis;

public class BookManagementDao {
	private Jedis jedis;
	private Gson gson;

	public BookManagementDao() {
		
		Properties properties = Config.loadProperties();

		String redisPort = properties.getProperty("redis.book.port");
		String redisPassword = properties.getProperty("redis.book.password");
		
		jedis = new Jedis("redis://default:" + redisPassword +  "@redis-15460.c61.us-east-1-3.ec2.cloud.redislabs.com:" + redisPort);
        System.out.println("Reddis Connection successfull in BookManagementDao");
		this.gson = new Gson();

	}
	public boolean saveBook(Book book) {
		int bookID = jedis.incr("bookID").intValue();;
		book.setBookId(bookID);
		String bookJson = gson.toJson(book);
		String result = jedis.set("book:" + book.getBookId(), bookJson);
		return "OK".equals(result);
	}
	
	public Book loadBook(int id) {
		String bookJson = jedis.get("book:" + id);
        return bookJson != null ? gson.fromJson(bookJson, Book.class) : null;
	}
    public List<Book> getAllBooks() {
        // Get all keys that represent books
        Set<String> bookKeys = jedis.keys("book:" + "*");

        // Initialize a list to store books
        List<Book> books = new ArrayList<Book>();
        if (!bookKeys.isEmpty()) {
            for (String bookKey : bookKeys) {
                // Retrieve the book data from Redis using GET
                String bookJson = jedis.get(bookKey);

                // Convert the JSON string back to a Book object and add to the list
                Book book = gson.fromJson(bookJson, Book.class);
                books.add(book);
            }

            return books;
        }
        // Retrieve each book by key
        return null;
    }
	

}
