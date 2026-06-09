package view;

import controller.ProductoController;
import model.Producto;

public class ProductoView {

    private ProductoController controller;

    public ProductoView() {
        controller = new ProductoController();
    }

    public void mostrarProductos() {

        for (Producto p : controller.obtenerProductos()) {
            System.out.println(p);
        }
    }
}