package controller;

import modelo.*;

import java.util.Date;

public class OrdenCompraController {
    private SistemaGestionCompras sistema;

    public OrdenCompraController() {
        this.sistema = SistemaGestionCompras.getInstancia();
    }

    public OrdenCompra crearOrdenCompra(int numero, Proveedor proveedor) {
        OrdenCompra ordenCompra = new OrdenCompra(numero, new Date(), proveedor);
        sistema.agregarOrdenCompra(ordenCompra);
        return ordenCompra;
    }

    public void agregarLinea(OrdenCompra ordenCompra, Item item, int cantidad, double precioUnitarioAcordado) {
        ordenCompra.agregarLinea(item, cantidad, precioUnitarioAcordado);
    }

    public void confirmarOrdenCompra(OrdenCompra ordenCompra) {
        sistema.confirmarOrdenCompra(ordenCompra);
    }

    public void aprobarOrdenCompra(OrdenCompra ordenCompra, Usuario usuario) {
        ordenCompra.aprobarPorSupervisor(usuario);
    }
}