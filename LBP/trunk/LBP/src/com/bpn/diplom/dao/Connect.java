package com.bpn.diplom.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class Connect {
	public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	
	public static final String DB_NAME   = "lbp";
	public static final String PROTOCOL  = "jdbc:mysql";
	public static final String IP_ADRESS = "localhost";
	public static final String PORT 	 = "3306";
	public static final String USER = "root";
	public static final String PASS = "";
//	public static final String SEPARATOR = "/";
	
	
	static{
		try {
			Class.forName(JDBC_DRIVER).newInstance();
		} catch (InstantiationException e) {
			Logger.getRootLogger().error("InstantiationException при получении коннекта к базе"+JDBC_DRIVER+"\n",e);
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			Logger.getRootLogger().error("IllegalAccessException при получении коннекта к базе"+JDBC_DRIVER+"\n",e);
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Logger.getRootLogger().error("не найден "+JDBC_DRIVER+"\n",e);
			e.printStackTrace();
		}
	}
	
	public static Connection newInstance(){
		Connection connection = null;
		try {
			String url = PROTOCOL+"://"+IP_ADRESS+":"+PORT+"/"+DB_NAME;
			connection = DriverManager.getConnection(url, USER, PASS);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return connection;
	}
	
	
//	public static void main(String[] args) {
//	}
	
	
}
