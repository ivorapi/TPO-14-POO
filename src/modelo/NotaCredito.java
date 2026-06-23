package modelo;

import java.util.Date;

public class NotaCredito extends DocumentoComercial {
    private Factura facturaVinculada;

    public NotaCredito(Proveedor proveedor, Date fechaEmision, int nroDocumento, double importeTotal) {
        super(proveedor, fechaEmision, nroDocumento, importeTotal);
    }

    @Override
    public double impactoEnCuentaCorriente() {
        return getImporteTotal() * -1;
    }

    public void vincularFactura(Factura factura) {
        this.facturaVinculada = factura;
    }

    public Factura getFacturaVinculada() {
        return facturaVinculada;
    }
}