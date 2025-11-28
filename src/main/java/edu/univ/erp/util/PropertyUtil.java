package edu.univ.erp.util;
import java.io.*;
import java.util.*;

public class PropertyUtil {
    private static final Properties prop = new Properties();

    static {
        try (InputStream inputStream =
                     PropertyUtil.class.getClassLoader().getResourceAsStream("application.properties")) {

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new IllegalStateException("Configuration file 'application.properties' not found");
            }

        } catch (IOException e) {
            System.out.println("Error: Unable to load configuration from 'application.properties'.");
            System.out.println("Please check that the file exists in src/main/resources and is readable.");
        }
    }

    public static String get(String key) {
        return prop.getProperty(key);
    }

   
    private PropertyUtil() {}
}
