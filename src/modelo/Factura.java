package modelo;

import modelo.enums.EstadoDocumentoComercial;

import java.util.Date;

public class Factura extends DocumentoComercial {
    private OrdenCompra ordenCompra;

    public Factura(Proveedor proveedor, Date fechaEmision, int nroDocumento, double importeTotal) {
        super(proveedor, fechaEmision, nroDocumento, importeTotal);
    }

    @Override
    public double impactoEnCuentaCorriente() {
        return getImporteTotal();
    }

    public boolean validarConOC(OrdenCompra ordenCompra, Usuario usuario) {
        this.ordenCompra = ordenCompra;

        if (ordenCompra == null) {
            if (usuario != null && usuario.isSupervisor()) {
                cambiarEstado(EstadoDocumentoComercial.PENDIENTE);
                return true;
            }

            cambiarEstado(EstadoDocumentoComercial.OBSERVADO);
            return false;
        }

        if (!verificarCoherenciaConceptos(ordenCompra)) {
            cambiarEstado(EstadoDocumentoComercial.OBSERVADO);
            return false;
        }

        if (!validarPrecios(ordenCompra)) {
            if (usuario != null && usuario.isSupervisor()) {
                cambiarEstado(EstadoDocumentoComercial.PENDIENTE);
                return true;
            }

            cambiarEstado(EstadoDocumentoComercial.OBSERVADO);
            return false;
        }

        cambiarEstado(EstadoDocumentoComercial.PENDIENTE);
        return true;
    }

    public boolean verificarCoherenciaConceptos(OrdenCompra ordenCompra) {
        for (LineaOrdenCompra lineaFactura : getDetalleItems()) {
            LineaOrdenCompra lineaOC = buscarLineaOC(ordenCompra, lineaFactura.getItem());

            if (lineaOC == null) {
                return false;
            }
        }

        return true;
    }

    public boolean validarPrecios(OrdenCompra ordenCompra) {
        for (LineaOrdenCompra lineaFactura : getDetalleItems()) {
            LineaOrdenCompra lineaOC = buscarLineaOC(ordenCompra, lineaFactura.getItem());

            if (lineaOC == null) {
                return false;
            }

            double precioFactura = lineaFactura.getPrecioUnitarioAcordado();
            double precioOC = lineaOC.getPrecioUnitarioAcordado();

            if (Math.abs(precioFactura - precioOC) > 0.01) {
                return false;
            }
        }

        return true;
    }

    private LineaOrdenCompra buscarLineaOC(OrdenCompra ordenCompra, Item item) {
        for (LineaOrdenCompra lineaOC : ordenCompra.getLineas()) {
            if (lineaOC.getItem().getCodigo() == item.getCodigo()) {
                return lineaOC;
            }
        }

        return null;
    }

    public boolean estaObservada() {
        return getEstado() == EstadoDocumentoComercial.OBSERVADO;
    }

    public OrdenCompra getOrdenCompra() {
        return ordenCompra;
    }
}