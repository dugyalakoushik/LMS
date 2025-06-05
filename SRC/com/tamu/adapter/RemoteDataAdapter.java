package com.tamu.adapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tamu.dto.MyBookDto;
import com.tamu.entity.Book;
import com.tamu.entity.MembershipType;
import com.tamu.entity.User;

public class RemoteDataAdapter {
	Gson gson = new Gson();
	final int PORT = 8080;

	final String microserviceHost = "localhost";

	PrintWriter microserviceWriter;
	Socket microserviceSocket;
	BufferedReader microserviceReader;

	public void connect() {
		try {
			microserviceSocket = new Socket("localhost", PORT);
			microserviceWriter = new PrintWriter(microserviceSocket.getOutputStream(), true);
			microserviceReader = new BufferedReader(new InputStreamReader(microserviceSocket.getInputStream()));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void disconnect() {
		try {
			microserviceSocket.close();
			microserviceWriter.close();
			microserviceReader.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public User createMembership(String requestBody) throws UnknownHostException, IOException {
		String httpMethod = "POST";
		String path = "/user/membership";
		connect();
		String responseBody = doRESTCall(httpMethod, path, requestBody);

		User user = gson.fromJson(responseBody, User.class);
		disconnect();
		return user;
	}

	public User login(String requestBody) throws UnknownHostException, IOException {
		String httpMethod = "POST";
		String path = "/user/login";
		connect();
		String responseBody = doRESTCall(httpMethod, path, requestBody);

		User user = gson.fromJson(responseBody, User.class);
		disconnect();
		return user;
	}

	public String register(String requestBody) throws UnknownHostException, IOException {
		String httpMethod = "POST";
		String path = "/user/register";
		connect();
		String responseBody = doRESTCall(httpMethod, path, requestBody);
		disconnect();
		return responseBody;
	}

	public String addBook(String requestBody) throws UnknownHostException, IOException {
		String httpMethod = "POST";
		String path = "/book/addBook";
		connect();
		String responseBody = doRESTCall(httpMethod, path, requestBody);

		String response = gson.fromJson(responseBody, String.class);
		disconnect();
		return response;
	}

	public List<Book> getBooks() throws UnknownHostException, IOException {
		String httpMethod = "GET";
		String path = "/book/search/all";
		connect();
		String responseBody = doRESTCall(httpMethod, path, "");
		Type bookListType = new TypeToken<List<Book>>() {
		}.getType();
		List<Book> response = gson.fromJson(responseBody, bookListType);
		disconnect();
		return response;
	}
	
	public List<MyBookDto> getUserSubscriptions(String userId) throws UnknownHostException, IOException {

		String httpMethod = "GET";
		String path = "/subscription/books/userId/"+ userId;
		connect();
		String responseBody = null;
		try {
		responseBody = doRESTCall(httpMethod, path, "");
		}catch(Exception e) {
			System.out.println("Error!!");
		}
		Type bookListType = new TypeToken<List<MyBookDto>>() {
		}.getType();
		List<MyBookDto> response = null;
		if (responseBody != null) {
			 response = gson.fromJson(responseBody, bookListType);
		}
		disconnect();
		return response;
	}
	
	
	public List<Book> getBooks(String type, String searchString) throws UnknownHostException, IOException {
		String httpMethod = "GET";
		String path = "/book/search/" + type + "/" + searchString;
		connect();
		String responseBody = doRESTCall(httpMethod, path, "");
		Type bookListType = new TypeToken<List<Book>>() {
		}.getType();
		List<Book> response = gson.fromJson(responseBody, bookListType);
		disconnect();
		return response;
	}

	public List<MembershipType> retrieveMemberships() {
		List<MembershipType> memberships = List.of(new MembershipType(3, "Platinum Membership", 365, 4, 99.99),
				new MembershipType(2, "Gold Membership", 90, 3, 28.99),
				new MembershipType(1, "Silver Member", 30, 3, 10.99));
		return memberships;
	}

	public String subscribe(String requestBody) throws UnknownHostException, IOException {
    	String httpMethod = "POST";
    	String path = "/subscription/subscribe";
		connect();
		String responseBody = doRESTCall(httpMethod, path, requestBody);
	    disconnect();
	    return responseBody;
	}
	public String unsubscribe(String subscriptionId) throws UnknownHostException, IOException {
		String httpMethod = "GET";
		String path = "/subscription/unsubscribe/" + subscriptionId;
		connect();
		String responseBody = doRESTCall(httpMethod, path, "");
		disconnect();
		return responseBody;
	}

	private String doRESTCall(String httpMethod, String path, String requestBody) {
		try {


			microserviceWriter.println(httpMethod + " " + path + " HTTP/1.1");
			microserviceWriter.println("Host: " + microserviceHost);
			microserviceWriter.println("Content-Type: application/json"); // Change as needed
			microserviceWriter.println("Content-Length: " + requestBody.length());
			microserviceWriter.println();

			if (httpMethod.equals("POST")) {
				microserviceWriter.print(requestBody.toString());
				microserviceWriter.println(); // Ensure a blank line after the request body
				microserviceWriter.flush();
			}

			StringBuilder response = new StringBuilder();
			String line;
			while ((line = microserviceReader.readLine()) != null) {
				response.append(line).append("\r\n");
			}

			String fullResponse = response.toString();
			String responseBody = null;
			// Find the blank line index to separate headers and body
			int headerBodySeparatorIndex = fullResponse.indexOf("\r\n\r\n");
			if (headerBodySeparatorIndex != -1) {
				responseBody = fullResponse.substring(headerBodySeparatorIndex + 4); // +4 to move past the "\r\n\r\n"

				System.out.println("Response Body: " + responseBody);
			} else {
				System.err.println("No body found in response");
			}

			microserviceWriter.close();
			microserviceReader.close();
			microserviceSocket.close();

			return responseBody;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
