package controller;

import modelo.*;

import java.util.ArrayList;
import java.util.Date;

public class OrdenPagoController {
    private ArrayList<OrdenPago> ordenesPago;

    public OrdenPagoController() {
        this.ordenesPago = new ArrayList<>();
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
            ordenesPago.add(ordenPago);
        }

        return emitida;
    }

    public ArrayList<OrdenPago> consultarPagosPorProveedor(Proveedor proveedor) {
        ArrayList<OrdenPago> resultado = new ArrayList<>();

        for (OrdenPago ordenPago : ordenesPago) {
            if (ordenPago.getProveedor().getCuit() == proveedor.getCuit()) {
                resultado.add(ordenPago);
            }
        }

        return resultado;
    }

    public ArrayList<OrdenPago> getOrdenesPago() {
        return ordenesPago;
    }
}