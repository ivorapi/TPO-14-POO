package controller;

import modelo.*;
import modelo.enums.EstadoDocumentoComercial;

import java.util.ArrayList;
import java.util.Date;

public class DocumentoComercialController {
    private ArrayList<DocumentoComercial> documentosComerciales;

    public DocumentoComercialController() {
        this.documentosComerciales = new ArrayList<>();
    }

    public Factura crearFactura(int nroDocumento, Proveedor proveedor) {
        return new Factura(proveedor, new Date(), nroDocumento, 0);
    }

    public void agregarLineaFactura(Factura factura, Item item, int cantidad, double precioUnitario) {
        factura.agregarLinea(item, cantidad, precioUnitario);
    }

    public boolean validarYRegistrarFactura(Factura factura, OrdenCompra ordenCompra, Usuario usuario) {
        boolean esValida = factura.validarConOC(ordenCompra, usuario);
        documentosComerciales.add(factura);
        return esValida;
    }

    public NotaDebito crearNotaDebito(int nroDocumento, Proveedor proveedor, double importeTotal) {
        NotaDebito notaDebito = new NotaDebito(proveedor, new Date(), nroDocumento, importeTotal);
        documentosComerciales.add(notaDebito);
        return notaDebito;
    }

    public NotaCredito crearNotaCredito(int nroDocumento, Proveedor proveedor, double importeTotal) {
        NotaCredito notaCredito = new NotaCredito(proveedor, new Date(), nroDocumento, importeTotal);
        documentosComerciales.add(notaCredito);
        return notaCredito;
    }

    public DocumentoComercial buscarDocumentoPorNumero(int nroDocumento) {
        for (DocumentoComercial documento : documentosComerciales) {
            if (documento.getNroDocumento() == nroDocumento) {
                return documento;
            }
        }

        return null;
    }

    public double calcularDeudaActualProveedor(Proveedor proveedor) {
        double deuda = 0;

        for (DocumentoComercial documento : documentosComerciales) {
            if (documento.getProveedor().getCuit() == proveedor.getCuit()) {
                if (documento.getEstado() != EstadoDocumentoComercial.CANCELADO) {
                    deuda = deuda + documento.impactoEnCuentaCorriente();
                    deuda = deuda - documento.getMontoPagado();
                }
            }
        }

        return deuda;
    }

    public ArrayList<DocumentoComercial> obtenerDocumentosPendientes() {
        ArrayList<DocumentoComercial> pendientes = new ArrayList<>();

        for (DocumentoComercial documento : documentosComerciales) {
            if (documento.getEstado() != EstadoDocumentoComercial.CANCELADO) {
                pendientes.add(documento);
            }
        }

        return pendientes;
    }

    public ArrayList<DocumentoComercial> getDocumentosComerciales() {
        return documentosComerciales;
    }
}