package com.ssafy.happyhouse.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DBUtil2 {
	static String url = "jdbc:mysql://localhost:3306/happyhouse?serverTimezone=UTC&useUniCode=yes&characterEncoding=UTF-8&useSSL=false&allowPublicKeyRetrieval=true";
	static String user = "ssafy";
	static String pw = "ssafy";
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("driver loading 실패");
		}
	}
	public static Connection getConnection() throws SQLException {
		DataSource dataSource = null;
		try {
			Context  iCtx = new InitialContext();
			if(iCtx!= null) {
				Context ctx = (Context)iCtx.lookup("java:comp/env");
				dataSource = (DataSource)ctx.lookup("jdbc/ssafy");
				return dataSource.getConnection();
			}else {
				return DriverManager.getConnection(url, user, pw);
			}
		} catch (Exception e) {
				return DriverManager.getConnection(url, user, pw);
		}
	}

	public static void close(AutoCloseable... closeables) {
		for (AutoCloseable c : closeables) {
			if (c != null) {
				try {
					c.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static void rollback(Connection con) {
		if (con != null) {
			try {
				con.rollback();
			} catch (Exception e) {
			}
		}
	}
}
