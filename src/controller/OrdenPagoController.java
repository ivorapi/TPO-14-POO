package controller;

import modelo.*;

import java.util.Date;

public class OrdenPagoController {
    private SistemaGestionCompras sistema;

    public OrdenPagoController() {
        this.sistema = SistemaGestionCompras.getInstancia();
    }

    public OrdenPago crearOrdenPago(int numero, Proveedor proveedor) {
        return new OrdenPago(numero, proveedor, new Date());
    }

    public void agregarDocumentoAPagar(OrdenPago ordenPago, DocumentoComercial documento, double montoAplicado) {
        ordenPago.agregarDetallePago(documento, montoAplicado);
    }

    public void calcularRetenciones(OrdenPago ordenPago) {
        ordenPago.calcularRetenciones();
    }

    public void agregarMedioPago(OrdenPago ordenPago, MedioPago medioPago) {
        ordenPago.agregarMedioPago(medioPago);
    }

    public boolean emitirOrdenPago(OrdenPago ordenPago) {
        boolean emitida = ordenPago.emitir();

        if (emitida) {
            sistema.agregarOrdenPago(ordenPago);
        }

        return emitida;
    }
}