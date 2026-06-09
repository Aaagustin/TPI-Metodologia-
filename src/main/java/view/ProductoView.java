package view;
import controller.ProductoController;

import java.util.Scanner;

public class ProductoView {

    private ProductoController controller;

    public ProductoView() {
        controller = new ProductoController();
    }

    public void iniciar() {

        Scanner teclado = new Scanner(System.in);

        int opcion;

        do {
            System.out.println("1- Agregar producto");
            System.out.println("2- Listar productos");
            System.out.println("3- Salir");

            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {

                case 1:
                    agregarProducto(teclado);
                    break;

                case 2:
                    listarProductos();
                    break;
            }

        } while (opcion != 3);
    }

    private void agregarProducto(Scanner teclado) {
    }

    private void listarProductos() {
    }
}