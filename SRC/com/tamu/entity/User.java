package com.tamu.entity;

import org.bson.Document;

public class User {
	private int userId;
	private String password;
	private String username;
	private String firstName;
	private String lastName;
	private int age;
	private String email;
	private String activity;
	private MembershipType membershipType;
	private int membershipId;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public MembershipType getMembership() {
		return membershipType;
	}
	public void setMembership(MembershipType membership) {
		this.membershipType = membership;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public User(int userId,  String username, String password,  String firstName, String lastName, int age,
			String activity, String email) {
		super();
		this.userId = userId;
		this.password = password;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.age = age;
		this.activity = activity;
		this.email = email;
	}
	public User() {
		// TODO Auto-generated constructor stub
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getActivity() {
		return activity;
	}
	public void setActivity(String activity) {
		this.activity = activity;
	}
    public Document toDocument() {
        return new Document("userId", userId)
                .append("password", password)
                .append("username", username)
                .append("firstName", firstName)
                .append("lastName", lastName)
                .append("age", age)
                .append("email", email)
                .append("activity", activity)
                .append("membershipId", membershipId);
    }

    public static User fromDocument(Document document) {
        User user = new User();
        user.setUserId(document.getInteger("userId"));
        user.setPassword(document.getString("password"));
        user.setUsername(document.getString("username"));
        user.setFirstName(document.getString("firstName"));
        user.setLastName(document.getString("lastName"));
        user.setAge(document.getInteger("age"));
        user.setEmail(document.getString("email"));
        user.setActivity(document.getString("activity"));
        user.setMembershipId(document.getInteger("membershipId"));
        return user;
    }
	public int getMembershipId() {
		return membershipId;
	}
	public void setMembershipId(int membershipId) {
		this.membershipId = membershipId;
	}
	

}
