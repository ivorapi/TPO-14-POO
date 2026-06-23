package modelo;

import java.util.Date;

public class NotaDebito extends DocumentoComercial {

    public NotaDebito(Proveedor proveedor, Date fechaEmision, int nroDocumento, double importeTotal) {
        super(proveedor, fechaEmision, nroDocumento, importeTotal);
    }

    @Override
    public double impactoEnCuentaCorriente() {
        return getImporteTotal();
    }

    public void aplicarIntereses(float porcentaje) {
        double nuevoImporte = getImporteTotal() + (getImporteTotal() * porcentaje / 100);
        setImporteTotal(nuevoImporte);
    }
}