package com.alura.jdbc.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alura.jdbc.factory.ConnectionFactory;


public class ProductoController {

	public int modificar(String nombre, String descripcion, Integer cantidad ,Integer id) throws SQLException {
		ConnectionFactory factory = new ConnectionFactory();
		Connection con = factory.recuperaConnection();
		
		PreparedStatement statement = con.prepareStatement(
				"UPDATE TB_Producto SET "
				+ "nombre = ?"
				+ ", descripcion = ?"
				+ ", cantidad = ? "
				+ "WHERE id = id"
				);
		statement.setString(1, nombre);
		statement.setString(2, descripcion);
		statement.setInt(3, cantidad);
		statement.setInt(4, id);
		
		statement.execute();
		
		int updateCount = statement.getUpdateCount();
		con.close();
		return updateCount;
	}

	public int eliminar(Integer id) throws SQLException {
		ConnectionFactory factory = new ConnectionFactory();
		Connection con = factory.recuperaConnection();
		
		PreparedStatement statement = con.prepareStatement(
				"DELETE FROM TB_Producto WHERE ID = ?"
				);
		statement.setInt(1, id);
		
		statement.execute();
		
		return statement.getUpdateCount();
	}

//	the method returns a Map
	public List<Map<String, String>> listar() throws SQLException {
		ConnectionFactory factory = new ConnectionFactory();
		Connection con = factory.recuperaConnection();
		
		PreparedStatement statement = con.prepareStatement(
				"SELECT id, nombre, descripcion, cantidad FROM TB_Producto");
		
		statement.execute();
		
		ResultSet resulset = statement.getResultSet();
		
		ArrayList<Map<String, String>> resultado = new ArrayList<>();
		
		while(resulset.next()) {
			Map<String, String> fila = new HashMap<>();
			fila.put("id", String.valueOf(resulset.getInt("id")));
			fila.put("nombre", resulset.getString("nombre"));
			fila.put("descripcion", resulset.getString("descripcion"));
			fila.put("cantidad", String.valueOf(resulset.getInt("cantidad")));
			resultado.add(fila);
		}
		
		con.close();
		return resultado;
	}

    public void guardar(Map<String, String> producto) throws SQLException {
    	ConnectionFactory factory = new ConnectionFactory();
		Connection con = factory.recuperaConnection();
		
//		No SQLInjection
		PreparedStatement statement =  con.prepareStatement(
				  "INSERT INTO TB_Producto "
				+ "(nombre, descripcion, cantidad)"
				+ "VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
		statement.setString(1, producto.get("nombre"));
		statement.setString(2, producto.get("descripcion"));
		statement.setInt(3, Integer.valueOf(producto.get("cantidad")));
		
		statement.execute();
		ResultSet resultset = statement.getGeneratedKeys();
		
		while(resultset.next()) {
			System.out.println(String.format("Fue insertado el producto de ID %d",
					resultset.getInt(1)));
		}
	}

}
