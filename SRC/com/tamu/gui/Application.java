package com.tamu.gui;
import java.sql.*;

import com.tamu.adapter.RemoteDataAdapter;
import com.tamu.entity.User;

public class Application {

    private static Application instance;

    public static Application getInstance() {
        if (instance == null) {
            instance = new Application();
        }
        return instance;
    }

    private Connection connection;

    public Connection getDBConnection() {
        return connection;
    }

    private RemoteDataAdapter adapter;

    private User currentUser = null;

    public User getCurrentUser() { return currentUser; }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public LoginScreen loginScreen = new LoginScreen();

    public LoginScreen getLoginScreen() {
        return loginScreen;
    }

    private Application() {
    		
        adapter = new RemoteDataAdapter();
    }


    public static void main(String[] args) {
        Application.getInstance().getLoginScreen().setVisible(true);
    }
    
    public RemoteDataAdapter getDataAdapter() {
        return adapter;
    }
}
