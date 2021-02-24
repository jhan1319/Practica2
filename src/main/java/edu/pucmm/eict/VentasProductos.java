package edu.pucmm.eict;

import java.util.ArrayList;
import java.util.Date;

public class VentasProductos {

    long id;
    Date fechaCompra;
    String nombreCliente;
    CarroCompra carrito = null;
    static int autoIncrement = 1;

    public VentasProductos( Date fechaCompra, String nombreCliente, CarroCompra carrito) {
        this.id = VentasProductos.autoIncrement;
        this.fechaCompra = fechaCompra;
        this.nombreCliente = nombreCliente;
        this.carrito= carrito;
        autoIncrement++;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public CarroCompra getCarrito() {
        return carrito;
    }

    public void setCarrito(CarroCompra carrito) {
        this.carrito = carrito;
    }

    public static int getAutoIncrement() {
        return autoIncrement;
    }

    public static void setAutoIncrement(int autoIncrement) {
        VentasProductos.autoIncrement = autoIncrement;
    }
}
