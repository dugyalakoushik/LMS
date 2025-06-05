package com.tamu.dto;

import com.tamu.entity.Address;
import com.tamu.entity.Card;
import com.tamu.entity.User;

public class MembershipDto {
	private Address address;
	private Card card;
	private User user;
	
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public Card getCard() {
		return card;
	}
	public void setCard(Card card) {
		this.card = card;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
