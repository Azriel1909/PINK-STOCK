package com.alura.jdbc.controller;

import java.util.List;


import com.alura.jdbc.factory.ConnectionFactory;
import com.alura.jdbc.modelo.Producto;

import como.alura.jdbc.dao.ProductoDAO;


public class ProductoController {

	private ProductoDAO productoDao;
	
	public ProductoController() {
		this.productoDao = new ProductoDAO(new ConnectionFactory().recuperaConnection());
	}
	
	public int modificar(String nombre, String descripcion, Integer cantidad ,Integer id) {
		return productoDao.modificar(nombre, descripcion, cantidad, id);
	}

	public int eliminar(Integer id) {	
		return productoDao.eliminar(id);
	}

	public List<Producto> listar() {
		return productoDao.listar();
	}

    public void guardar(Producto producto) {
		productoDao.guardar(producto);
    }
}
