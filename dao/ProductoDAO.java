package com.alura.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import com.alura.jdbc.factory.ConnectionFactory;
import com.alura.jdbc.modelo.Producto;

public class ProductoDAO {
	final private Connection con;
	
	public ProductoDAO(Connection con) {
		this.con = con;
	}
	
	public void guardar(Producto producto) {
			try(con){
//				No transaction control
//				con.setAutoCommit(false);
				
//				No SQLInjection
				final PreparedStatement statement =  con.prepareStatement(
						  "INSERT INTO TB_Producto "
						+ "(nombre, descripcion, cantidad, categoria_id)"
						+ "VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
				
					try(statement) {
						statement.setString(1, producto.getNombre());
						statement.setString(2, producto.getDescripcion());
						statement.setInt(3, producto.getCantidad());
						statement.setInt(4, producto.getCategoriaId());
						statement.execute();
						final ResultSet resultset = statement.getGeneratedKeys();
						try(resultset){
							while(resultset.next()) {
								producto.setId(resultset.getInt(1));
								System.out.println(String.format("Fue insertado el producto %s", producto));
							}
						}
					} 	
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
	}


	public List<Producto> listar() {
		List<Producto> resultado = new ArrayList<>();
		
		ConnectionFactory factory = new ConnectionFactory();
		final Connection con = factory.recuperaConnection();
		
		try(con){
			final PreparedStatement statement = con.prepareStatement(
					"SELECT id, nombre, descripcion, cantidad FROM TB_Producto");
			try(statement){
			
				statement.execute();
				
				final ResultSet resulset = statement.getResultSet();
				
				try(resulset){
					while(resulset.next()) {
						Producto fila = new Producto(
								resulset.getInt("id"),
								resulset.getString("nombre"),
								resulset.getString("descripcion"),
								resulset.getInt("cantidad")
								);
						
						resultado.add(fila);
					}
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return resultado;
	}

	public int eliminar(Integer id) {
		try {
			final PreparedStatement statement = con.prepareStatement(
					"DELETE FROM TB_Producto WHERE id = ?"
					);
			try(statement){
				statement.setInt(1, id);
				statement.execute();
				
				int updateCount = statement.getUpdateCount();
				
				return updateCount;
			}
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}

	public int modificar(String nombre, String descripcion, Integer cantidad, Integer id) {		
		
		try {
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
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Producto> listar(Integer categoriaId) {
		
List<Producto> resultado = new ArrayList<>();
		
		ConnectionFactory factory = new ConnectionFactory();
		final Connection con = factory.recuperaConnection();
		
		try(con){
			var querySelect = "SELECT id, nombre, descripcion, cantidad "
					+ " FROM TB_Producto"
					+ " WHERE categoria_id = ?";
			
			final PreparedStatement statement = con.prepareStatement(
					querySelect);
			System.out.println(querySelect);
			try(statement){
				statement.setInt(1, categoriaId);
				statement.execute();
				
				final ResultSet resulset = statement.getResultSet();
				
				try(resulset){
					while(resulset.next()) {
						Producto fila = new Producto(
								resulset.getInt("id"),
								resulset.getString("nombre"),
								resulset.getString("descripcion"),
								resulset.getInt("cantidad")
								);
						
						resultado.add(fila);
					}
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return resultado;
		
	}

//	WARNING
}
