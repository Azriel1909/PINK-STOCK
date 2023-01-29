package como.alura.jdbc.dao;

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
						+ "(nombre, descripcion, cantidad)"
						+ "VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
				
					try(statement) {
						ejecutaRegistro(producto, statement);
						
					} 	
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
	}
	
	private void ejecutaRegistro(Producto producto, PreparedStatement statement)
			throws SQLException {
/*		ERROR logic test
 		if(cantidad < 50) {
		throw new RuntimeException("ERROR!");
		}
*/
		statement.setString(1, producto.getNombre());
		statement.setString(2, producto.getDescripcion());
		statement.setInt(3, producto.getCantidad());
		
		statement.execute();
		
		final ResultSet resultset = statement.getGeneratedKeys();
		
		try(resultset){
			while(resultset.next()) {
				producto.setId(resultset.getInt(1));
				System.out.println(String.format("Fue insertado el producto %s", producto));
			}
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
			return resultado;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
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

//	WARNING
}
