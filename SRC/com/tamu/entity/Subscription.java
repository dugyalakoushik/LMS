package com.tamu.entity;

import java.util.Date;

import org.bson.Document;

public class Subscription {

	private int subscriptionId;
	private Book book;
	private User user;
	private String orderDate;
	private String dueDate;
	private int isReturned;
	public int getIsReturned() {
		return isReturned;
	}
	public void setIsReturned(int isReturned) {
		this.isReturned = isReturned;
	}
	public long getSubscriptionId() {
		return subscriptionId;
	}
	public void setSubscriptionId(int orderId) {
		this.subscriptionId = orderId;
	}	
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}


    public Document toDocument() {
        return new Document("subscriptionId", subscriptionId)
                .append("book", book.toDocument())
                .append("user", user.toDocument())
                .append("orderDate", orderDate)
                .append("dueDate", dueDate)
                .append("isReturned", isReturned);
    }


}
