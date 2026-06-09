
package Models;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class Carrito {
   
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
    
    
    
}
