package com.alura.jdbc.pruebas;

import java.sql.Connection;
import java.sql.SQLException;

import com.alura.jdbc.factory.ConnectionFactory;

public class PruebaPoolDeConexiones {
	public static void main(String[] args) throws SQLException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		for (int i = 0; i < 20; i++) {
			Connection con = connectionFactory.recuperaConnection();
			System.out.println("Conexión inciada: #" + (i +1));
//			limits the number of connections - The database is not saturated
		}
	}
}
