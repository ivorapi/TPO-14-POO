package controller;

import modelo.*;

import java.util.Date;

public class DocumentoComercialController {
    private SistemaGestionCompras sistema;

    public DocumentoComercialController() {
        this.sistema = SistemaGestionCompras.getInstancia();
    }

    public Factura crearFactura(int nroDocumento, Proveedor proveedor) {
        return new Factura(proveedor, new Date(), nroDocumento, 0);
    }

    public void agregarLineaFactura(Factura factura, Item item, int cantidad, double precioUnitario) {
        factura.agregarLinea(item, cantidad, precioUnitario);
    }

    public boolean validarYRegistrarFactura(Factura factura, OrdenCompra ordenCompra, Usuario usuario) {
        boolean esValida = factura.validarConOC(ordenCompra, usuario);
        sistema.agregarDocumentoComercial(factura);
        return esValida;
    }

    public NotaDebito crearNotaDebito(int nroDocumento, Proveedor proveedor, double importeTotal) {
        NotaDebito notaDebito = new NotaDebito(proveedor, new Date(), nroDocumento, importeTotal);
        sistema.agregarDocumentoComercial(notaDebito);
        return notaDebito;
    }

    public NotaCredito crearNotaCredito(int nroDocumento, Proveedor proveedor, double importeTotal) {
        NotaCredito notaCredito = new NotaCredito(proveedor, new Date(), nroDocumento, importeTotal);
        sistema.agregarDocumentoComercial(notaCredito);
        return notaCredito;
    }
}