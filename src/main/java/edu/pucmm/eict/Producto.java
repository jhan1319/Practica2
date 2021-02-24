package edu.pucmm.eict;

public class Producto {

    int id;
    String nombre;
    float precio;
    int quantity;
    static int autoIncrement = 1;

    public Producto(String nombre, float precio, int quantity) {
        this.id = Producto.autoIncrement;
        this.nombre = nombre;
        this.precio = precio;
        this.quantity = quantity;
        Producto.autoIncrement++;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    private int eliminarProducto(int cantidad){
        int cant = this.quantity - cantidad;
        return cant;
    }

}
