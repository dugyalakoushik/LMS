package com.tamu.dao;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final String PROPERTIES_FILE = "config.properties";

    public static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                System.out.println("Sorry, unable to find " + PROPERTIES_FILE);
                return properties;
            }

            // load a properties file from class path, inside static method
            properties.load(input);
        } catch (Exception e) {
            e.printStackTrace();
        }	
        return properties;
    }
}
