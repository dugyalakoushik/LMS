package com.tamu.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.tamu.dao.BookManagementDao;
import com.tamu.dto.MembershipDto;
import com.tamu.entity.Address;
import com.tamu.entity.Book;
import com.tamu.entity.Card;
import com.tamu.entity.Membership;
import com.tamu.entity.User;

public class BookService {

	private static final int PORT = 8082;
	private static final String SERVICE_NAME = "book_service";
	private static final Gson gson = new Gson();
	private static BookManagementDao dataAdapter = null;

	public static void main(String[] args) throws IOException {
		ServiceRegistry.registerService(SERVICE_NAME, PORT);
		System.out.println("Book Service is registered with the Service Registry.");

		ServerSocket listenSocket = new ServerSocket(PORT);
		System.out.println("Book Service is listening on port " + PORT);
		dataAdapter = new BookManagementDao();

		while (true) {
//            Socket clientSocket = serverSocket.accept();
			Socket connectionSocket = listenSocket.accept();
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

			String requestMessageLine = inFromClient.readLine();
			System.out.println("Request: " + requestMessageLine);

			if (requestMessageLine == null) {
				connectionSocket.close();
				continue;
			}

			String[] requestTokens = requestMessageLine.split(" ");
			String httpMethod = requestTokens[0];
			String requestPath = requestTokens[1];
			Map<String, String> headers = new HashMap<>();
			String headerLine;
			while ((headerLine = inFromClient.readLine()) != null && !headerLine.isEmpty()) {
				String[] header = headerLine.split(": ", 2);
				if (header.length > 1) {
					headers.put(header[0], header[1]);
				}
			}
			if (httpMethod.equals("GET") && requestPath.startsWith("/book/search/bookName/")) {
				String bookName = requestPath.substring("/book/search/bookName/".length());
				List<Book> allBooks = dataAdapter.getAllBooks();
				if (allBooks != null) {
					List<Book> books = searchBooksbyBookName(bookName, allBooks);
					String jsonResponse = gson.toJson(books);
					sendJsonResponse(outToClient, jsonResponse);

				} else {
					sendError(outToClient, "Book Not Found", 404);

				}
			} else if (httpMethod.equals("GET") && requestPath.startsWith("/book/search/authorName/")) {
				String authorName = requestPath.substring("/book/search/authorName/".length());
				List<Book> allBooks = dataAdapter.getAllBooks();
				if (allBooks != null) {
					List<Book> books = searchBooksbyAuthorName(authorName, allBooks);
					String jsonResponse = gson.toJson(books);
					sendJsonResponse(outToClient, jsonResponse);

				} else {

					sendError(outToClient, "Book Not Found", 404);

				}
			} else if (httpMethod.equals("GET") && requestPath.startsWith("/book/search/all")) {
				List<Book> allBooks = dataAdapter.getAllBooks();
				if (allBooks != null) {
					String jsonResponse = gson.toJson(allBooks);
					sendJsonResponse(outToClient, jsonResponse);
				} else {

					sendError(outToClient, "Book Not Found", 404);

				}
			} else if (httpMethod.equals("POST") && requestPath.equals("/book/addBook")) {
				String contentLengthLine = headers.get("Content-Length");
				int contentLength = Integer.parseInt(contentLengthLine.trim());
				char[] bodyChars = new char[contentLength];
				inFromClient.read(bodyChars);
				String requestBody = new String(bodyChars);
				addBook(requestBody, outToClient);
				sendJsonResponse(outToClient, "Book Added Successfully");

			} else {
				sendError(outToClient, "Invalid request.", 400);
			}

			connectionSocket.close();
		}

	}

	private static void sendJsonResponse(DataOutputStream outToClient, String jsonResponse) throws IOException {
		outToClient.writeBytes("HTTP/1.1 200 OK\r\n");
		outToClient.writeBytes("Content-Type: application/json\r\n");
		outToClient.writeBytes("Content-Length: " + jsonResponse.length() + "\r\n");
		outToClient.writeBytes("\r\n");
		outToClient.writeBytes(jsonResponse);
	}

	private static void sendError(DataOutputStream outToClient, String message, int statusCode) throws IOException {
		String jsonRes = "{\"error\": \"" + message + "\"}";
		String res = "HTTP/1.1 " + statusCode + " Error\r\n" + "Content-Type: application/json\r\n" + "Content-Length: "
				+ jsonRes.length() + "\r\n" + "\r\n" + jsonRes;
		outToClient.writeBytes(res);
		outToClient.flush();
	}

	private static boolean containsUrlDecodedIgnoreCase(String source, String target) {
		try {
			String decodedSource = URLDecoder.decode(source, "UTF-8");
			String decodedTarget = URLDecoder.decode(target, "UTF-8");
			return decodedSource.toLowerCase().contains(decodedTarget.toLowerCase());
		} catch (UnsupportedEncodingException e) {
			// Handle the exception as needed
			e.printStackTrace();
			return false;
		}
	}

	public static List<Book> searchBooksbyBookName(String searchTerm, List<Book> books) {
		List<Book> result = new ArrayList<>();
		for (Book book : books) {
			if (containsUrlDecodedIgnoreCase(book.getBookName(), searchTerm)) {
				result.add(book);
			}
		}

		return result;
	}

	public static List<Book> searchBooksbyAuthorName(String searchTerm, List<Book> books) {
		List<Book> result = new ArrayList<>();
		for (Book book : books) {
			if (containsUrlDecodedIgnoreCase(book.getAuthorName(), searchTerm)) {
				result.add(book);
			}
		}

		return result;
	}
	   private static void addBook(String requestBody, DataOutputStream outToClient) throws IOException {
	       try {
	           Book book = gson.fromJson(requestBody, Book.class);
	           dataAdapter.saveBook(book);
				} catch(Exception e) {
					sendError(outToClient, "Error Creating Membership", 500);
				}
	       } 

}
