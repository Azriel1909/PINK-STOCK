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
		final Connection con = factory.recuperaConnection();
		
		try(con){
		
			final PreparedStatement statement = con.prepareStatement(
					"UPDATE TB_Producto SET "
					+ "nombre = ? "
					+ ", descripcion = ? "
					+ ", cantidad = ? "
					+ "WHERE id = ? "
					);
			try(statement){
				statement.setString(1, nombre);
				statement.setString(2, descripcion);
				statement.setInt(3, cantidad);
				statement.setInt(4, id);
				
				statement.execute();
				
				int updateCount = statement.getUpdateCount();
				
				return updateCount;
			}
		}
	}

	public int eliminar(Integer id) throws SQLException {
		ConnectionFactory factory = new ConnectionFactory();
		final Connection con = factory.recuperaConnection();
		
		try(con){
		
			final PreparedStatement statement = con.prepareStatement(
					"DELETE FROM TB_Producto WHERE  = ?"
					);
			try(statement){
				statement.setInt(1, id);
				
				statement.execute();
				
				int updateCount = statement.getUpdateCount();
				
				return updateCount;
			}
		}
	}

//	the method returns a Map
	public List<Map<String, String>> listar() throws SQLException {
		ConnectionFactory factory = new ConnectionFactory();
		final Connection con = factory.recuperaConnection();
		
		try(con){
			final PreparedStatement statement = con.prepareStatement(
					"SELECT id, nombre, descripcion, cantidad FROM TB_Producto");
			try(statement){
			
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
				return resultado;
				
			}
		}
	}

    public void guardar(Map<String, String> producto) throws SQLException {
    	String nombre = producto.get("nombre");
    	String descripcion = producto.get("descripcion");
    	Integer cantidad = Integer.valueOf(producto.get("cantidad"));
    	Integer cantidadMaxima = 50;
    	
    	ConnectionFactory factory = new ConnectionFactory();
		final Connection con = factory.recuperaConnection();
		
		try(con){
		
//			No transaction control
			con.setAutoCommit(false);
			
//			No SQLInjection
			final PreparedStatement statement =  con.prepareStatement(
					  "INSERT INTO TB_Producto "
					+ "(nombre, descripcion, cantidad)"
					+ "VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
			
			
				try(statement) {
				do {
						int cantidadParaGuardar = Math.min(cantidad, cantidadMaxima);
						ejecutaRegistro(nombre, descripcion, cantidadParaGuardar, statement);
						cantidad -= cantidadMaxima;
				} while(cantidad > 0);

					con.commit();
					System.out.println("COMMIT!");
				} catch (Exception e) {
					con.rollback();
					System.out.println("ROLLBACK!");
				}	
				
		}
	}

	private void ejecutaRegistro(String nombre, String descripcion, Integer cantidad, PreparedStatement statement)
			throws SQLException {
		
//		if(cantidad < 50) {
//			throw new RuntimeException("ERROR!");
//		}
		
		statement.setString(1, nombre);
		statement.setString(2, descripcion);
		statement.setInt(3, cantidad);
		
		statement.execute();
		
		final ResultSet resultset = statement.getGeneratedKeys();
		
		try(resultset){
			while(resultset.next()) {
				System.out.println(String.format("Fue insertado el producto de ID %d",
						resultset.getInt(1)));
			}
		}
	}

}
