package controller;

import model.Producto;
import repository.ProductoRepository;

import java.util.List;

public class ProductoController {

    private ProductoRepository repository;

    public ProductoController() {
        repository = new ProductoRepository();
    }

    public void agregarProducto(Producto producto) {
        repository.guardar(producto);
    }

    public List<Producto> obtenerProductos() {
        return repository.listar();
    }
}