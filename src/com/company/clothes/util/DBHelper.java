package com.company.clothes.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;


public class DBHelper {
   
	 // 设置成私有的
	 private static final String url = "jdbc:mysql://127.0.0.1:3306/jk";
	 private static final String username = "root";
	 private static final String password = "12345";
	 private static Connection conn = null; 
	 // 静态代码块加载数据库驱动
	 static {
		 try {
			 Class.forName("com.mysql.cj.jdbc.Driver");
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	 }

    public static Connection getConnection() throws SQLException {
    	 Driver driver = new com.mysql.cj.jdbc.Driver();
         DriverManager.registerDriver(driver);

         //2.通过DriverManager与MySql服务器之间建立一个连接通道
         //格式 jdbc:mysql://服务器IP地址:端口号/数据库
         
         if (conn == null) {
        	 Connection conn = DriverManager.getConnection(url,username,password);
        	 return conn;
         }
         return conn;
    }
    
    public static void checkConnection(Connection conn) {
    	  try {
    		  if (conn != null) {
    			  System.out.println("数据库连接正常");
    		  } else {
    			  System.out.println("数据库连接异常");
    		  }
    	  	}catch (Exception e) {
    	  		e.printStackTrace();
    	  		}
    	 	}

    
    public static void close(ResultSet rs, PreparedStatement ps, Connection conn) throws Exception
    {
        try
        {
            conn.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
	public static void main(String[] arg){
		 try{
			 Connection conn = DBHelper.getConnection();
			 DBHelper.checkConnection(conn);

		 }catch(SQLException throwables){
			 throwables.printStackTrace();
		 }
	}
}
