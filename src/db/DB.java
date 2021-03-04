package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DB {
    private static Connection conn = null;

    public static Connection getConnection(){
        if (conn == null){
            try {
                Properties prop = loadProperties();
                String url = prop.getProperty("dburl");
                conn = DriverManager.getConnection(url,prop);

            } catch (SQLException e){
                throw new DBException(e.getMessage());
            }
        }
        return conn;
    }

    public static Connection closeConnection(){
        try {
            if (conn != null){
                conn.close();
            }
        } catch (SQLException e){
            throw new DBException(e.getMessage());
        }
        return conn;
    }

    private static Properties loadProperties(){
        try (FileInputStream fs = new FileInputStream("src/db.properties")){
            Properties prop = new Properties();
            prop.load(fs);
            return prop;
        } catch (IOException e){
            throw new DBException(e.getMessage());
        }
    }

    public static void closeStatement (Statement t){
        if(t != null){
            try {
                t.close();
            } catch (SQLException e) {
                throw new DBException(e.getMessage());
            }
        }
    }
    public static void closeResultSet (ResultSet rs){
        if(rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                throw new DBException(e.getMessage());
            }
        }
    }
}