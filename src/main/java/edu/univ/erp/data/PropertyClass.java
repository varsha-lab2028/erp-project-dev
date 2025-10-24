/*also connects to the MySQL server*/
package edu.univ.erp.data;
import java.io.*;
import java.util.*;

public class PropertyClass {
    private static final Properties prop = new Properties();
    //static block
    static {
        try (InputStream input_stream = PropertyClass.class.getClassLoader().getResourceAsStream("application.properties")){
            if(input_stream!=null){
                prop.load(input_stream);
            }
        } catch (IOException e) {
            e.printStackTrace(); //prints the error instead of ignoring it
        }
    }
    static String get(String s){
        return prop.getProperty(s);
    }

    //constructor
    private PropertyClass() {}
}
