package com.alura.jdbc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.alura.jdbc.modelo.Categoria;

public class CategoriaDAO {
	
	private Connection con;

    public CategoriaDAO(Connection con) {
        this.con = con;
    }

	public List<Categoria> listar() {
		List<Categoria> resultado = new ArrayList<>();
		
		try {
			var querySelect = "SELECT id, nombre FROM TB_Categoria";
			System.out.println(querySelect);
			final PreparedStatement statement = con.prepareStatement(querySelect
					);
//			Direct Access
			
			try(statement) {
				final ResultSet resultSet = statement.executeQuery();
				try(resultSet){
					while(resultSet.next()) {
						var categoria = new Categoria(
								resultSet.getInt("id"),
								resultSet.getString("nombre"));
						resultado.add(categoria);
					}
				};
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		return resultado;
	}
	
}
