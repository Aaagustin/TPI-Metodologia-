package repository;

import model.Producto;
import java.util.ArrayList;
import java.util.List;

public class ProductoRepository {

    private List<Producto> productos;

    public ProductoRepository() {
        productos = new ArrayList<>();
    }

    public void guardar(Producto producto) {
        productos.add(producto);
    }

    public List<Producto> obtenerTodos() {
        return productos;
    }
}