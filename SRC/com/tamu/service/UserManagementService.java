package com.tamu.service;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.tamu.dao.UserManagementDao;
import com.tamu.dto.MembershipDto;
import com.tamu.entity.Address;
import com.tamu.entity.Card;
import com.tamu.entity.Membership;
import com.tamu.entity.User;

public class UserManagementService {

    private static final Gson gson = new Gson();
    private static UserManagementDao dataAdapter = null;

    public static void main(String args[]) throws Exception {
        int myPort = 8081; // Change the port as needed
		ServiceRegistry.registerService("user_management_service", myPort);
		System.out.println("User Management Service is registered with the Service Registry.");

        ServerSocket listenSocket = new ServerSocket(myPort);
        System.out.println("User Management service waiting for request on port " + myPort);
        dataAdapter = new UserManagementDao();

        while (true) {
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
            if (httpMethod.equals("POST") && requestPath.equals("/user/register")) {
                String contentLengthLine = headers.get("Content-Length");
                    int contentLength = Integer.parseInt(contentLengthLine.trim());
                    char[] bodyChars = new char[contentLength];
                    inFromClient.read(bodyChars);
                    String requestBody = new String(bodyChars);
                        registerUser(requestBody, outToClient);
            }
            else if (httpMethod.equals("POST") && requestPath.equals("/user/login")) {
                String contentLengthLine = headers.get("Content-Length");
                    int contentLength = Integer.parseInt(contentLengthLine.trim());
                    char[] bodyChars = new char[contentLength];
                    inFromClient.read(bodyChars);
                    String requestBody = new String(bodyChars);
                        loginUser(requestBody, outToClient);

            }else if (httpMethod.equals("POST") && requestPath.equals("/user/membership")) {
                String contentLengthLine = headers.get("Content-Length");
                    int contentLength = Integer.parseInt(contentLengthLine.trim());
                    char[] bodyChars = new char[contentLength];
                    inFromClient.read(bodyChars);
                    String requestBody = new String(bodyChars);
                        createMembership(requestBody, outToClient);

            } else if (httpMethod.equals("GET") && requestPath.startsWith("/user/membership/")) {
				String userId = requestPath.substring("/user/membership/".length());
				Membership membership = dataAdapter.getMembershipByUserId(Integer.parseInt(userId));
				if (membership != null) {
					String jsonResponse = gson.toJson(membership);
					sendJsonResponse(outToClient, jsonResponse);
				} else {
					sendError(outToClient, "Membership Not Found", 404);
				}
			} else {
                sendError(outToClient, "Invalid request.", 400);
            }

            connectionSocket.close();
        }
    }
    
    private static void registerUser(String requestBody, DataOutputStream outToClient) throws IOException {
        try {
            User newUser = gson.fromJson(requestBody, User.class);

            if (dataAdapter.isUsernameTaken(newUser.getUsername())) {
                sendError(outToClient, "Username is already taken", 400);
                return;
            }
            if (dataAdapter.isEmailTaken(newUser.getEmail())) {
            	sendError(outToClient, "Email is already taken", 400);
                return;
            }
            if (dataAdapter.saveUser(newUser)) {
                sendJsonResponse(outToClient, "User Registered Successfully");
            } else {
                sendError(outToClient, "Error registering user", 500);
            }
        } catch (Exception e) {
        	sendError(outToClient, "Bad Request: Error registering user", 400);
        }
    }
    
    private static void loginUser(String requestBody, DataOutputStream outToClient) throws IOException {
        try {
            User loginAttempt = gson.fromJson(requestBody, User.class);
            System.out.println("Login Attempt: " + loginAttempt.getUsername() + ", " + loginAttempt.getPassword());
            User existingUser = dataAdapter.loadUser(loginAttempt.getUsername(), loginAttempt.getPassword());
            if (existingUser != null) {          
					String jsonResponse = gson.toJson(existingUser);
					sendJsonResponse(outToClient, jsonResponse);
			} else {
				sendError(outToClient, "Invalid username or password", 401);
			}
        } catch (Exception e) {
            sendError(outToClient, "Bad Request: Error logging in", 400);
        }
    }
    private static void createMembership(String requestBody, DataOutputStream outToClient) throws IOException {
        try {
            MembershipDto dto = gson.fromJson(requestBody, MembershipDto.class);
            Address address = dto.getAddress();
            dataAdapter.saveAddress(address);
            Card card = dto.getCard();
            dataAdapter.saveCard(card);
            Membership membership = new Membership();
            membership.setUserId((int)dto.getUser().getUserId());
            Calendar calendar = Calendar.getInstance();
            Date startDate = new Date(); // Current date as a java.util.Date
            calendar.setTime(startDate);

            calendar.add(Calendar.DAY_OF_MONTH, dto.getUser().getMembership().getValidity());

            Date endDate = calendar.getTime();
            membership.setStartDate(startDate);
            membership.setEndDate(endDate);
            int membershipId = dataAdapter.saveMembership(membership);
            User user = dto.getUser();
            user.setMembershipId(membershipId);
            dataAdapter.updateUser(user);
            String jsonResponse = gson.toJson(user);
			sendJsonResponse(outToClient,jsonResponse);

			} catch(Exception e) {
				sendError(outToClient, "Error Creating Membership", 500);
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
        String res = "HTTP/1.1 " + statusCode + " Error\r\n" + "Content-Type: application/json\r\n" +
                "Content-Length: " + jsonRes.length() + "\r\n" + "\r\n" + jsonRes;
        outToClient.writeBytes(res);
        outToClient.flush();
    }
   

}

