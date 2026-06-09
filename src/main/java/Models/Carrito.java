
package Models;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class Carrito implements Serializable {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCarrito;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "fk_carrito")
    private List<itemCarrito> item;
    private double monto;

    public Carrito(int idCarrito, List<itemCarrito> item, double monto) {
        this.idCarrito = idCarrito;
        this.item = item;
        this.monto = monto;
    }

    public Carrito() {
    }

    public int getIdCarrito() {
        return idCarrito;
    }

    public void setIdCarrito(int idCarrito) {
        this.idCarrito = idCarrito;
    }

    public List<itemCarrito> getItem() {
        return item;
    }

    public void setItem(List<itemCarrito> item) {
        this.item = item;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }
    
    
    public void calcularTotal() {
        double suma = 0.0;
        if (this.item != null) {
            for (itemCarrito i : this.item) {
                if (i.getProducto() != null) {
                    suma += i.getProducto().getPrecio() * i.getCantidad();
                }
            }
        }
        this.monto = suma;
    }

    public void agregarProducto(Producto producto, int cantidad) {
        if (producto == null || cantidad <= 0) return;
        
        itemCarrito itemExistente = null;
        for (itemCarrito i : this.item) {
            if (i.getProducto() != null && i.getProducto().getIdProducto() == producto.getIdProducto()) {
                itemExistente = i;
                break;
            }
        }
        
        if (itemExistente != null) {
            itemExistente.setCantidad(itemExistente.getCantidad() + cantidad);
        } else {
            itemCarrito nuevoItem = new itemCarrito(producto, cantidad);
            nuevoItem.setCarrito(this);
            this.item.add(nuevoItem);
        }
        this.calcularTotal();
    }

    public void modificarCantidad(int idItem, int nuevaCantidad) {
        if (nuevaCantidad <= 0) {
            this.quitarItemPorId(idItem);
            return;
        }
        for (itemCarrito i : this.item) {
            if (i.getIdItem() == idItem) {
                i.setCantidad(nuevaCantidad);
                break;
            }
        }
        this.calcularTotal();
    }

    public void quitarItemPorId(int idItem) {
        itemCarrito itemAQuitar = null;
        for (itemCarrito i : this.item) {
            if (i.getIdItem() == idItem) {
                itemAQuitar = i;
                break;
            }
        }
        if (itemAQuitar != null) {
            this.item.remove(itemAQuitar);
            this.calcularTotal();
        }
    }

    public void vaciarCarrito() {
        this.item.clear();
        this.monto = 0.0;
    }

    public boolean puedeConfirmarPedido() {
        if (this.item == null || this.item.isEmpty()) return false;
        for (itemCarrito i : this.item) {
            if (i.getCantidad() <= 0 || i.getProducto() == null) return false;
        }
        return true;
    }
    
    
}
