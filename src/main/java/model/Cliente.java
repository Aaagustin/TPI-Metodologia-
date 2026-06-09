package model;

import java.util.ArrayList;
import java.util.List;

public class Cliente {

    private Long idCliente;
    private String nombre;
    private String apellido;
    private String email;
    private List<Direccion> listaDirecciones;

    public Cliente(Long idCliente, String nombre, String apellido, String email) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.listaDirecciones = new ArrayList<>();
    }

    public void agregarDireccion(Direccion direccion) {
        listaDirecciones.add(direccion);
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Direccion> getListaDirecciones() {
        return listaDirecciones;
    }

    public void setListaDirecciones(List<Direccion> listaDirecciones) {
        this.listaDirecciones = listaDirecciones;
    }

    @Override
    public String toString() {
        return nombre + " " + apellido;
    }
}