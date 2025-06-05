package com.tamu.entity;

public class Address {
	private int addressId;
	private int userId;
	private String lineOne;
	private String city;
	private String state;
	private String zip;
	private String country;
	private String contact;
	public String getContact() {
		return contact;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getLineOne() {
		return lineOne;
	}
	public void setLineOne(String lineOne) {
		this.lineOne = lineOne;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getZip() {
		return zip;
	}
	public Address (String lineOne, String city, String state, String zip, String country,
			String contact) {
		super();
		this.lineOne = lineOne;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.country = country;
		this.contact = contact;
	}
	public Address() {
		// TODO Auto-generated constructor stub
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}

	public int getAddressId() {
		return addressId;
	}
	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}
}
