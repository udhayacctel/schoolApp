package com.sstutor.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	public static Connection getDBConnection() throws SQLException, ClassNotFoundException
	{
		Class.forName("org.postgresql.Driver");
		return DriverManager.getConnection(Constants.DB_URL, Constants.DB_UID, Constants.DB_PD);
	}
}
