package com.alura.jdbc.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	public Connection recuperaConnection() throws SQLException {
//		Design Pattern: Factory Method - Encapsulation - It centralizes the logic in one center
		return DriverManager.getConnection(
				"jdbc:mysql://localhost/Control_PinkStock?useTimeZone=true&serverTimeZone=UTC", 
				"root", 
				"Sunflower.1959");
	}
}
