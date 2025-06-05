package com.tamu.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private static final String URL = "jdbc:postgresql://localhost:5432/LMS";
	private static final String USER = "postgres";
	private static final String PASSWORD = "password";
	
	public static Connection getConnectionToDataBase() throws ClassNotFoundException, SQLException {
		Connection connection = null;
		
		Class.forName("org.postgresql.Driver");
		System.out.println("PostgreSQL Driver Registered");
		connection = DriverManager.getConnection(URL,USER,PASSWORD);
		return connection;
	}
	
	
	
	
}
