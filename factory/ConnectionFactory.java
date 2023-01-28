package com.alura.jdbc.factory;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ConnectionFactory {
	
	private DataSource dataSource;
	
	public ConnectionFactory(){
		var pooledDataSource = new ComboPooledDataSource();
		pooledDataSource.setJdbcUrl("jdbc:mysql://localhost/Control_PinkStock?useTimeZone=true&serverTimeZone=UTC");
		pooledDataSource.setUser("root");
		pooledDataSource.setPassword("Sunflower.1959");
		pooledDataSource.setMaxPoolSize(10);
		
		this.dataSource = pooledDataSource;
	}
	
	public Connection recuperaConnection() throws SQLException {
//		Design Pattern: Factory Method - Encapsulation - It centralizes the logic in one center
		return this.dataSource.getConnection();
	}
}
