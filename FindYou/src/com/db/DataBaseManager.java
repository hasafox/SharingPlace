package com.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseManager {
    public static final String DRIVER = "com.mysql.jdbc.Driver";
    public static final String USER = "user"; //user is your database user name
    public static final String PASS = "pass"; //pass is your database password
    public static final String URL = "jdbc:mysql://ip:3306/android?useUnicode=true&characterEncoding=utf-8&useSSL=false"; //ip is your server`s ip
    private Connection conn = null;
    private Statement stmt = null;

    public void connDataBase() {
        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            System.out.println( "driver false");
            e.printStackTrace();
        } catch (SQLException e) {
        	System.out.println("conn false");
            e.printStackTrace();
        }
    }

    public void closeDataBase() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
        	System.out.println("close false");
            e.printStackTrace();
        }
    }

    public boolean selectSQL(String sql) {
        ResultSet rs;
        try {
            connDataBase();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                closeDataBase();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDataBase();
        return false;
    }

    public boolean handleSQL(String sql) {
        int result;
        try {
            connDataBase();
            stmt = conn.prepareStatement(sql);
            result = stmt.executeUpdate(sql);
            if (result == 1) {
                closeDataBase();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeDataBase();
        return false;
    }
    
    public String msgSQL(String sql) {
    	 ResultSet rs;
    	 String result = null;
         try {
             connDataBase();
             stmt = conn.prepareStatement(sql);
             rs = stmt.executeQuery(sql);
             while(rs.next()){
             result = rs.getString(1);
             return result;
             }
             
         } catch (SQLException e) {
             e.printStackTrace();
         }
         closeDataBase();
         return result;
     }
    
    public String friendsSQL(String sql) {
    	ResultSet rs;
    	String result = "";
    	try {
			connDataBase();
			stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
				result += rs.getString(1) + ":" + rs.getString(2) + ":";
			}
            return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	 closeDataBase();
         return result;
	}
    public String friendSQL(String sql) {
    	ResultSet rs;
    	String result = "";
    	try {
			connDataBase();
			stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
				result += rs.getString(1) + ":";
			}
            return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	 closeDataBase();
         return result;
	}
    
}
