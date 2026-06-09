package Models;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class itemCarrito {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idItem;
    
    private int cantidad;

    
    @ManyToOne
    @JoinColumn(name = "fk_carrito", insertable = false, updatable = false)
    private Carrito carrito;

    
    @ManyToOne
    @JoinColumn(name = "fk_producto")
    private Producto producto;

   

    public itemCarrito() {
    }


    public itemCarrito(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }


    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}