package edu.pucmm.eict;

import java.util.ArrayList;

public class Tienda {
    ArrayList<Producto> productosController;
    ArrayList<Usuario> usuariosController;
    ArrayList<CarroCompra> carrosComprasController;
    ArrayList<VentasProductos> ventasProductosController;

    public Tienda() {
        this.productosController = new ArrayList<>();
        this.usuariosController = new ArrayList<>();
        this.carrosComprasController = new ArrayList<>();
        this.ventasProductosController = new ArrayList<>();
    }

    public ArrayList<Producto> getProductosController() {
        return productosController;
    }

    public void setProductosController(ArrayList<Producto> productosController) {
        this.productosController = productosController;
    }

    public ArrayList<Usuario> getUsuariosController() {
        return usuariosController;
    }

    public void setUsuariosController(ArrayList<Usuario> usuariosController) {
        this.usuariosController = usuariosController;
    }

    public ArrayList<CarroCompra> getCarrosComprasController() {
        return carrosComprasController;
    }

    public void setCarrosComprasController(ArrayList<CarroCompra> carrosComprasController) {
        this.carrosComprasController = carrosComprasController;
    }

    public ArrayList<VentasProductos> getVentasProductosController() {
        return ventasProductosController;
    }

    public void setVentasProductosController(ArrayList<VentasProductos> ventasProductosController) {
        this.ventasProductosController = ventasProductosController;
    }

    public void agregarProductoStock(String nombre, float precio, int cant){
            Producto e = new Producto(nombre, precio, cant);
        productosController.add(e);

    }
    public void eliminarStockProductos(String nombre){
        productosController.removeIf(producto -> producto.getNombre().contentEquals(nombre));
    }
    public Producto prodByID(int id){
        Producto producto = null;
        for (Producto p: productosController) {
            if (p.getId()==id) {
                producto =p;

            }
        }
        return producto;
    }
    public VentasProductos ventasID(int id){
        VentasProductos venta = null;
        for (VentasProductos v: ventasProductosController) {
            if (v.getId()==id) {
                venta =v;

            }
        }
        return venta;
    }

    public void agregarVentas(VentasProductos v){

        ventasProductosController.add(v);
    }



}
